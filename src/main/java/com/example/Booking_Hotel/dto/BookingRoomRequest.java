package com.example.Booking_Hotel.dto;

import com.example.Booking_Hotel.model.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRoomRequest {
    private String bookingId;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Integer totalDay;

    private Room room;

    private Double price;

    private Integer quantity;

    private String paymentType;
}
