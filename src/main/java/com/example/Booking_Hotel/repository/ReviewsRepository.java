package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.Reviews;
import com.example.Booking_Hotel.model.Room;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
    List<Reviews> findByRoomId(Integer roomId);

    @Query("SELECT COUNT(v) FROM Reviews v")
    public Integer countReviews();
}
