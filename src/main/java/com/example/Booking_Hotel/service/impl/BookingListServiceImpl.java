package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.repository.BookingListRepository;
import com.example.Booking_Hotel.repository.RoomRepository;
import com.example.Booking_Hotel.repository.UserRepository;
import com.example.Booking_Hotel.service.BookingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingListServiceImpl implements BookingListService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingListRepository bookingListRepository;


    @Override
    public BookingList saveBookingList(Integer roomId, Integer userId) {
        Room room = roomRepository.findById(roomId).get();
        UserDtls user = userRepository.findById(userId).get();
        BookingList bookingListSts =bookingListRepository.findByRoomIdAndUserId(roomId,userId);

        BookingList bookingList = null;
        if (ObjectUtils.isEmpty(bookingListSts)) {
            bookingList = new BookingList();
            bookingList.setRoom(room);
            bookingList.setUser(user);
            bookingList.setQuantity(1);
            bookingList.setTotalPrice(1*room.getDiscountPrice());
        }else{
            bookingList = bookingListSts;
            bookingList.setQuantity(bookingList.getQuantity()+1);
            bookingList.setTotalPrice(bookingList.getQuantity()*bookingList.getRoom().getDiscountPrice());
        }
        BookingList saveBookingList= bookingListRepository.save(bookingList);




        return saveBookingList;
    }

    @Override
    public List<BookingList> getBookingListByUser(Integer userId) {
        List<BookingList> books = bookingListRepository.findByUserId(userId);

        Double totalBookedPrice = 0.0;
        List<BookingList> updateBookingLists = new ArrayList<>();

        for (BookingList b: books){
            Double totalPrice = (b.getRoom().getDiscountPrice()*b.getQuantity());
            b.setTotalPrice(totalPrice);

            totalBookedPrice += totalPrice;
            b.setTotalBookedPrice(totalBookedPrice);
            updateBookingLists.add(b);
        }
        return updateBookingLists;
    }


    @Override
    public void updateQuantity(String sy, Integer bid) {
        BookingList bookingLists = bookingListRepository.findById(bid).get();

        int updateQuantity;
        if (sy.equalsIgnoreCase("de")){

             updateQuantity = bookingLists.getQuantity()-1;

             if (updateQuantity <=0){
              bookingListRepository.delete(bookingLists);
             }else{
                 bookingLists.setQuantity(updateQuantity);
                 bookingListRepository.save(bookingLists);
             }
        }else{
            updateQuantity = bookingLists.getQuantity()+1;
            bookingLists.setQuantity(updateQuantity);
            bookingListRepository.save(bookingLists);
        }
        }

    @Override
    public void deleteAllByUserId(Integer userId) {
        List<BookingList> all = bookingListRepository.findByUserId(userId);
        bookingListRepository.deleteAll(all);

    }
}
