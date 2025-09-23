package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByRoomType(String roomType);

    List<Room> findByIsActiveTrue();

    Page<Room> findByRoomType(Pageable pageable, String roomType);

    Page<Room> findByIsActiveTrue(Pageable pageable);

    @Query("select r from Room r where (lower(r.title) like lower(concat('%',:keyword,'%') ) "
            +"or lower(r.roomType) like lower(concat('%',:keyword,'%')))")
    public Page<Room> searchRoomBy(String keyword,Pageable pageable);

    @Query("SELECT SUM(r.roomNumber) FROM Room r")
    public Integer totalRoomNumber();

}
