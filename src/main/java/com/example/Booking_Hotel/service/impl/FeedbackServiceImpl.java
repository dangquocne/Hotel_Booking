package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.model.Feedback;
import com.example.Booking_Hotel.repository.FeedbackRepository;
import com.example.Booking_Hotel.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setDate(LocalDate.now());
        return  feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public Boolean deleteFeedback(Integer id) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(feedback)) {
            feedbackRepository.delete(feedback);
            return true;
        }
        return false;
    }

    @Override
    public void  deleteAllFeedback() {
        List<Feedback> all = feedbackRepository.findAll();
        feedbackRepository.deleteAll(all);
    }

    @Override
    public Integer getCountFeedbacks() {
        return feedbackRepository.countFeedback();
    }
}
