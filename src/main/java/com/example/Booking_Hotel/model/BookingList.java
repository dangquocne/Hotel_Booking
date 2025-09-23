package com.example.Booking_Hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class BookingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserDtls user;

    @ManyToOne
    private  Room room;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalBookedPrice;
}
