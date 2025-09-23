package com.example.Booking_Hotel.dto;

import com.example.Booking_Hotel.model.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReviewsRequest {
private Integer id;

    private String comment;

    private Integer rating;

    private Room room;

}
