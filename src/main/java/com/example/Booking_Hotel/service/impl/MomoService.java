package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.client.MomoApi;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.dto.CreateMomoRequest;
import com.example.Booking_Hotel.dto.CreateMomoResponse;
import com.example.Booking_Hotel.model.BookingRoom;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {

    private final MomoApi momoApi;

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
private static final String REDIRECT_URL = "https://momo.vn/return";
    private static final String IPN_URL = "https://callback.url/notify";
    // private static final String REQUEST_TYPE = "captureWallet";
    private static final String REQUEST_TYPE = "payWithMethod";



    public CreateMomoResponse createQR(BookingRoom bookingRoom) {
        String orderId = bookingRoom.getBookingId();
        String orderInfo = "Thanh toán đơn đăt phòng: "+orderId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "Không có khuyến mãi gì hết";
        long amount = (long) (bookingRoom.getPrice()* bookingRoom.getQuantity()* bookingRoom.getTotalDay());

        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                ACCESS_KEY, amount, extraData, IPN_URL, orderId, orderInfo, PARTNER_CODE, REDIRECT_URL, requestId, REQUEST_TYPE);

        String prettySignature = "";
        try {
            prettySignature = signHmacSHA256(rawSignature, SECRET_KEY);
        } catch (Exception e) {
            log.error(">>>>>Co loi khi hash code: " + e);
            return null;
        }

        if (prettySignature.isBlank()) {
            log.error(">>>>> signature is blank");
            return null;
        }
        CreateMomoRequest request = CreateMomoRequest.builder()
                .partnerCode(PARTNER_CODE)
                .requestType(REQUEST_TYPE)
                .ipnUrl(IPN_URL)
                .redirectUrl(REDIRECT_URL)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .extraData(extraData)
                .amount(amount)
                .signature(prettySignature) // bạn sẽ cần thêm hàm tạo signature HMAC SHA256 vào đây
                .lang("vi")
                .build();

        return momoApi.createMomoQr(request);
    }

    private String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
