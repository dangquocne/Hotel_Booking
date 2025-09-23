package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("SELECT COUNT(f) FROM Feedback f")
    public Integer countFeedback();
}
