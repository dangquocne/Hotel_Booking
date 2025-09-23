package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.dto.BookingRequest;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.model.BookingRoom;
import com.example.Booking_Hotel.model.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingRoomService {

    public BookingRoom saveBookingRoom(Integer userId, BookingRoomRequest bookingRoomRequest, BookingRequest request) throws Exception;

    public  void saveBookingRoomForBookingList(Integer userId, BookingRoomRequest bookingRoomRequest, BookingRequest request) throws Exception;

    public List<BookingRoom> getBookingRoomsByUser(Integer userId) ;

    public BookingRoom updateBookingStatus(Integer id,String status);

    public List<BookingRoom> getAllBookingRooms();

    public Page<BookingRoom> getAllBookingRoomsPagination(Integer pageNo, Integer pageSize);

    public Page<BookingRoom> searchBookingRoomPagination(String ch, Integer pageNo, Integer pageSize);

    public Integer countBookingRoomByStatus();

    public Integer countBookingRoomCancelled();

    public Double totalBookingRevenue();

    public List<Integer>  getBookingsPerMonth(Integer year);


}
