package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.dto.BookingRequest;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.dto.ReviewsRequest;
import com.example.Booking_Hotel.model.Reviews;

import java.util.List;

public interface ReviewsService {

    public void saveReviews(Integer userId, ReviewsRequest reviewsRequest);

    public List<Reviews> getAllReviews();

    public List<Reviews> getReviewsByRoomId(Integer roomId);

    public Reviews getReviewsById(Integer reviewsId);

    public void updateReview(ReviewsRequest reviewsRequest);

    public void deleteReview(Integer reviewsId);

    public void deleteAllReview();

    public Integer getCountReviews();

}
