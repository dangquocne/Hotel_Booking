package com.example.Booking_Hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BookingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String  bookingId;

    private LocalDate bookingDate;

    private LocalDate checkIn;

    private LocalDate checkOut;

    @ManyToOne
    private Room room;

    private Double price;

    private Integer quantity;

    private Integer totalDay;

    @ManyToOne
    private UserDtls user;

    private String status;

    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private  BookingAdress bookingAdress;
}
