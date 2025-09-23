package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.dto.BookingRequest;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.model.*;
import com.example.Booking_Hotel.repository.BookingListRepository;
import com.example.Booking_Hotel.repository.BookingRoomRepository;
import com.example.Booking_Hotel.repository.RoomRepository;
import com.example.Booking_Hotel.repository.UserRepository;
import com.example.Booking_Hotel.service.BookingRoomService;
import com.example.Booking_Hotel.util.BookingStatus;
import com.example.Booking_Hotel.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingRoomServiceImpl implements BookingRoomService {
    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingListRepository bookingListRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public BookingRoom saveBookingRoom(Integer userId, BookingRoomRequest bookingRoomRequest, BookingRequest request) throws Exception {

        Room room = roomRepository.findById(bookingRoomRequest.getRoom().getId()).get();

        UserDtls userDtls = userRepository.findById(userId).get();

        BookingRoom bookingRoom= new BookingRoom();

        String shortId = generateNumberRicId(8);
//            bookingRoom.setBookingId(UUID.randomUUID().toString());
        bookingRoom.setBookingId("BID"+shortId);

        bookingRoom.setBookingDate(LocalDate.now());
        bookingRoom.setTotalDay(bookingRoomRequest.getTotalDay());
        bookingRoom.setCheckIn(bookingRoomRequest.getCheckIn());
        bookingRoom.setCheckOut(bookingRoomRequest.getCheckOut());

        bookingRoom.setRoom(room);
        bookingRoom.setQuantity(1);
        bookingRoom.setPrice(bookingRoomRequest.getRoom().getDiscountPrice());
       bookingRoom.setUser(userDtls);


       bookingRoom.setStatus(BookingStatus.IN_PROGRESS.getName());
        bookingRoom.setPaymentType(bookingRoomRequest.getPaymentType());

        BookingAdress bookingAdress = new BookingAdress();
        bookingAdress.setName(request.getName());
        bookingAdress.setEmail(request.getEmail());
        bookingAdress.setMobileNumber(request.getMobileNumber());


        bookingRoom.setBookingAdress(bookingAdress);

        BookingRoom saveBooking = bookingRoomRepository.save(bookingRoom);



            commonUtil.sendEmailForProductOrder(saveBooking, "success");

//        int newQuantity = room.getRoomNumber() - bookingRoom.getQuantity();
//        if (!ObjectUtils.isEmpty(saveBooking)) {
//            commonUtil.sendEmailForProductOrder(saveBooking,"success");
//             room.setRoomNumber(newQuantity);
//            roomRepository.save(room);
//        }


    return saveBooking;
    }


    @Override
    public void saveBookingRoomForBookingList(Integer userId, BookingRoomRequest bookingRoomRequest, BookingRequest request) throws Exception {
        List<BookingList> bookingLists = bookingListRepository.findByUserId(userId);
        BookingRoom saveBooking=null;
        for (BookingList booklist: bookingLists){
            BookingRoom bookingRoom = new BookingRoom();

            String shortId = generateNumberRicId(8);
//            bookingRoom.setBookingId(UUID.randomUUID().toString());
            bookingRoom.setBookingId("BID"+shortId);

            bookingRoom.setBookingDate(LocalDate.now());
            bookingRoom.setTotalDay(bookingRoomRequest.getTotalDay());
            bookingRoom.setCheckIn(bookingRoomRequest.getCheckIn());
            bookingRoom.setCheckOut(bookingRoomRequest.getCheckOut());


            bookingRoom.setRoom(booklist.getRoom());
            bookingRoom.setQuantity(booklist.getQuantity());
            bookingRoom.setPrice(booklist.getRoom().getDiscountPrice());
            bookingRoom.setUser(booklist.getUser());


            bookingRoom.setStatus(BookingStatus.IN_PROGRESS.getName());
            bookingRoom.setPaymentType(bookingRoomRequest.getPaymentType());

            BookingAdress bookingAdress = new BookingAdress();
            bookingAdress.setName(request.getName());
            bookingAdress.setEmail(request.getEmail());
            bookingAdress.setMobileNumber(request.getMobileNumber());


            bookingRoom.setBookingAdress(bookingAdress);

             saveBooking = bookingRoomRepository.save(bookingRoom);
            commonUtil.sendEmailForProductOrder(saveBooking,"success");

        }

//        bookingListRepository.deleteAll(bookingLists);

    }

    public String generateNumberRicId(Integer length){
        String digits = "0123456789";
       StringBuilder id = new StringBuilder();
        Random random = new Random();
       for (int i = 0; i < length; i++) {
           id.append(digits.charAt(random.nextInt(digits.length())));
       }

       return id.toString();
    }


    @Override
    public List<BookingRoom> getBookingRoomsByUser(Integer userId) {

        return bookingRoomRepository.findByUserId(userId);
    }

    @Override
    public BookingRoom updateBookingStatus(Integer id, String status) {
        Optional<BookingRoom> findById = bookingRoomRepository.findById(id);
        if (findById.isPresent()){
            BookingRoom bookingRoom = findById.get();
            bookingRoom.setStatus(status);
            BookingRoom updateStatus = bookingRoomRepository.save(bookingRoom);
            return updateStatus;
        }
        return null;
    }

    @Override
    public List<BookingRoom> getAllBookingRooms() {
        return bookingRoomRepository.findAll();
    }

    @Override
    public Page<BookingRoom> getAllBookingRoomsPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return  bookingRoomRepository.findAll(pageable);
    }

    @Override
    public Page<BookingRoom> searchBookingRoomPagination(String ch, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return bookingRoomRepository.searchBookingRoomBy(ch,pageable);
    }

    @Override
    public Integer countBookingRoomByStatus() {
        return bookingRoomRepository.countBookingRoomByStatus();
    }

    @Override
    public Integer countBookingRoomCancelled() {
        return bookingRoomRepository.countBookingRoomCancelled();
    }

    @Override
    public Double totalBookingRevenue() {
        return bookingRoomRepository.totalBookingRevenue();
    }

    @Override
    public List<Integer> getBookingsPerMonth(Integer year) {

        List<Object[]> rowMonth = bookingRoomRepository.countBookingRoomByMonth(year);
         List<Integer> result = new ArrayList<>(Collections.nCopies(12,0)); // 12 tháng = 0

        for (Object[] row : rowMonth){
            int month = (Integer) row[0];
            long count = (Long) row[1];
            result.set(month-1, (int) count);
        }

        return result;
    }


}
