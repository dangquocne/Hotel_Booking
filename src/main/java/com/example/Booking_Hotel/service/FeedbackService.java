package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.model.Feedback;

import java.util.List;

public interface FeedbackService {

    public Feedback saveFeedback(Feedback feedback);

    public List<Feedback> getAllFeedback();

    public Boolean deleteFeedback(Integer id);

    public void deleteAllFeedback();

    public Integer getCountFeedbacks();
}
