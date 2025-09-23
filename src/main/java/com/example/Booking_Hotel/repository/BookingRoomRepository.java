package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.model.BookingRoom;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.util.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, Integer> {

    public List<BookingRoom> findByUserId(Integer userId);

    public Page<BookingRoom> findAll(Pageable pageable);

    @Query("select b from BookingRoom b where (lower(b.bookingId) like lower(concat('%',:keyword,'%') ) "
            + "or lower(b.bookingAdress.name) like lower(concat('%',:keyword,'%')))")
    public Page<BookingRoom> searchBookingRoomBy(String keyword, Pageable pageable);


    @Query("SELECT COUNT(b) FROM BookingRoom b WHERE b.status='Approved' OR b.status='Completed'")
    public Integer countBookingRoomByStatus();

    @Query("SELECT COUNT(b) FROM BookingRoom b WHERE b.status='Cancelled' ")
    public Integer countBookingRoomCancelled();


    @Query("SELECT SUM(b.price*b.quantity*b.totalDay) FROM BookingRoom b WHERE b.status='Completed'")
    public Double totalBookingRevenue();


    @Query("SELECT MONTH(b.bookingDate),COUNT(b) FROM BookingRoom b where YEAR(b.bookingDate) = :year "
            + "and b.status='Approved' OR b.status='Completed'"
            + "GROUP BY MONTH(b.bookingDate)")
    public List<Object[]> countBookingRoomByMonth(@Param("year") Integer year);

}