package com.example.Booking_Hotel.util;

import com.example.Booking_Hotel.model.BookingRoom;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    String msg=null;
    public Boolean sendEmailForProductOrder(BookingRoom bookingRoom, String status )throws Exception {

        msg="<p>Hello [[name]],</p>"
                + "<p>Thank you booking <b>[[bookingStatus]]</b>.</p>"
                + "<p><b>Booking Details:</b></p>"
                + "<p>Name : [[roomName]]</p>"
                + "<p>Room Type : [[roomType]]</p>"
                + "<p>Quantity : [[quantity]]</p>"
                +"<p>Total Day : [[totalDay]]</p>"
                + "<p>Price : [[price]]</p>"
                + "<p>Payment Type : [[paymentType]]</p>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom("huynhdangquoctuan@gmail.com","Booking Hotel");
        helper.setTo(bookingRoom.getBookingAdress().getEmail());

        msg=msg.replace("[[name]]",bookingRoom.getBookingAdress().getName());
        msg=msg.replace("[[bookingStatus]]",status);
        msg=msg.replace("[[roomName]]",bookingRoom.getRoom().getTitle());
        msg=msg.replace("[[roomType]]",bookingRoom.getRoom().getRoomType());
        msg=msg.replace("[[quantity]]", bookingRoom.getQuantity().toString());
        msg=msg.replace("[[totalDay]]", bookingRoom.getTotalDay().toString());
        msg=msg.replace("[[price]]", bookingRoom.getPrice().toString());
        msg=msg.replace("[[paymentType]]",bookingRoom.getPaymentType());

        helper.setSubject("Booking Room Status");
        helper.setText(msg,true);
        mailSender.send(mimeMessage);
        return true;

    }

    public UserDtls getLoggedInUserDetails(Principal p){

        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }
}
