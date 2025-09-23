package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    Boolean existsByName(String name);

    List<RoomType> findByIsActiveTrue();

    @Query("SELECT COUNT(r) FROM RoomType r")
    public Integer countRoomTypes();
}
