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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String title;

    @Column(length = 5000)
    private String description;

    private String roomType;

    private Double price;

    private Integer roomNumber;

    private String image1;

    private String image2;

    private String image3;

    private Integer discount;

    private Double discountPrice;

    private Boolean isActive;
}
