package com.example.Booking_Hotel.client;

import com.example.Booking_Hotel.dto.CreateMomoRequest;
import com.example.Booking_Hotel.dto.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="momo", url = "https://test-payment.momo.vn/v2/gateway/api")
public interface MomoApi {

    @PostMapping("/create")
    CreateMomoResponse createMomoQr(@RequestBody CreateMomoRequest request);

}
