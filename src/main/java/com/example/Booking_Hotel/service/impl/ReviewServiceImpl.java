package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.dto.ReviewsRequest;
import com.example.Booking_Hotel.model.Reviews;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.repository.ReviewsRepository;
import com.example.Booking_Hotel.repository.RoomRepository;
import com.example.Booking_Hotel.repository.UserRepository;
import com.example.Booking_Hotel.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void saveReviews(Integer userId, ReviewsRequest reviewsRequest) {
        Room room = roomRepository.findById(reviewsRequest.getRoom().getId()).get();

        UserDtls userDtls = userRepository.findById(userId).get();
        Reviews reviews = new Reviews();
        reviews.setComment(reviewsRequest.getComment());
        reviews.setRating(reviewsRequest.getRating());
        reviews.setDate(LocalDate.now());
        reviews.setRoom(room);
        reviews.setUser(userDtls);

        reviewsRepository.save(reviews);
    }

    @Override
    public List<Reviews> getAllReviews() {
        return reviewsRepository.findAll();
    }

    @Override
    public List<Reviews> getReviewsByRoomId(Integer roomId) {
        return reviewsRepository.findByRoomId(roomId);
    }


    @Override
    public Reviews getReviewsById(Integer reviewsId) {

        return reviewsRepository.findById(reviewsId).get();
    }

    @Override
    public void updateReview(ReviewsRequest reviewsRequest) {
        Reviews oldReview = getReviewsById(reviewsRequest.getId());

        oldReview.setComment(reviewsRequest.getComment());
        oldReview.setRating(reviewsRequest.getRating());
        oldReview.setDate(LocalDate.now());
        reviewsRepository.save(oldReview);
    }

    @Override
    public void deleteReview(Integer reviewsId) {
        Reviews reviews = reviewsRepository.findById(reviewsId).get();
        reviewsRepository.delete(reviews);
    }

    @Override
    public void deleteAllReview() {
        List<Reviews> all = reviewsRepository.findAll();
        reviewsRepository.deleteAll(all);
    }

    @Override
    public Integer getCountReviews() {
        return reviewsRepository.countReviews();
    }
}
