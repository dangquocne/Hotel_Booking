package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.model.BookingList;

import java.util.List;

public interface BookingListService {

    public BookingList saveBookingList(Integer roomId , Integer userId);

    public List<BookingList> getBookingListByUser(Integer userId);

    public  void updateQuantity(String sy,Integer bid);

    public void deleteAllByUserId(Integer userId);


}
