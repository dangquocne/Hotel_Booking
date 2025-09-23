package com.example.Booking_Hotel.controller;

import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.dto.CreateMomoResponse;
import com.example.Booking_Hotel.service.impl.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/momo")
public class MomoController {
    @Autowired
    private  MomoService momoService;
//
//        @PostMapping("create")
//        public CreateMomoResponse createQR() {
//        return momoService.createQR(BookingRoomRequest bookingRoomRequest);
//        }
}
