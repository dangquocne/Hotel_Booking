package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingListRepository extends JpaRepository<BookingList, Integer> {

    public BookingList findByRoomIdAndUserId(Integer roomId, Integer userId);

    public List<BookingList> findByUserId(Integer userId);
}