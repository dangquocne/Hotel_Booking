package com.example.Booking_Hotel.controller;

import com.example.Booking_Hotel.dto.BookingRequest;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.dto.ReviewsRequest;
import com.example.Booking_Hotel.model.Feedback;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.service.*;
import com.example.Booking_Hotel.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private FeedbackService feedbackService;

    //lấy thông tin người dùng
    @ModelAttribute
    public void getUserDDetails(Principal p, Model model) {
        if (p!=null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
        }
    }

    //Hiển thị trang home, tìm kiếm phòng cùng với checkIn,Out,roomType, phân trang
    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String roomType,
                       @RequestParam(value = "submitted", required = false) Boolean submitted,
                       @ModelAttribute BookingRoomRequest roomRequest
                     , @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                       @RequestParam(value = "pageSize", defaultValue = "2")Integer pageSize ,HttpSession session ) {


        //hiển thị 4 phòng nổi bật len home
        List<Room>room = roomService.getAllActiveRoom("").stream().limit(4).toList();
        model.addAttribute("rooms", room);

        //        List<Room> roomList = roomService.getAllActiveRoom(roomType);

        //hiển thị data loại phòng lên khung tìm kiếm
        model.addAttribute("roomTypes", roomTypeService.getAllActiveRoomType());
        model.addAttribute("paramValue",roomType);


        //hiển thị  tât cả data phòng lên khung tìm kiếm
        Page<Room> roomsPage = roomService.getAllActiveRoomPagination(pageNo, pageSize, roomType);
        List<Room> rooms = roomsPage.getContent();//lấy nội dung sản phẩm



        //Kiểm tra  checkIn , checkOut, loại phòng để tìm phòng
        //sử dụng để tránh checkIn và checkOut không được null ban đầu
        if (Boolean.TRUE.equals(submitted)) {
             if ( ObjectUtils.isEmpty(roomRequest.getCheckIn()) || ObjectUtils.isEmpty(roomRequest.getCheckOut())) {
                 session.setAttribute("errorMsg", "Please enter valid date range");


             }
             else if (!roomRequest.getCheckOut().isAfter(roomRequest.getCheckIn())) {
                 session.setAttribute("errorMsg", "Check-In date must come before check-out date");

             }


            else{
                model.addAttribute("room", rooms );
                 model.addAttribute("roomSize", rooms.size());
                 session.removeAttribute("errorMsg");

            }
            }

        model.addAttribute("pageNo", roomsPage.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", roomsPage.getTotalElements());
        model.addAttribute("totalPages", roomsPage.getTotalPages());
        model.addAttribute("isFirst", roomsPage.isFirst());
        model.addAttribute("isLast", roomsPage.isLast());

        return "index";
    }


    @GetMapping("/signin")
    public String login() {
        return "login";
    }


    //hiển thị tất cả phòng lên trang hotel ,phân trang, lọc phòng theo loại phòng
    @GetMapping("/hotel")
    public String hotel(Model model, @RequestParam(required = false) String roomType,
                        @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize) {
//        List<Room> rooms=roomService.getAllActiveRoom(roomType);

        //Hển thị data loại phòng lên bộ lọc
        model.addAttribute("roomTypes", roomTypeService.getAllActiveRoomType());
        model.addAttribute("paramValue",roomType);

        //hIêển thị tất cả phòng
        Page<Room> roomsPage = roomService.getAllActiveRoomPagination(pageNo, pageSize, roomType);


        List<Room> rooms = roomsPage.getContent();//lấy nội dung sản phẩm

        model.addAttribute("rooms",rooms);


        model.addAttribute("pageNo", roomsPage.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", roomsPage.getTotalElements());
        model.addAttribute("totalPages", roomsPage.getTotalPages());
        model.addAttribute("isFirst", roomsPage.isFirst());
        model.addAttribute("isLast", roomsPage.isLast());

        return "hotel";
    }


    //hiển thị chi tiết phòng và tất cả đánh giá của khách hàng của chi tiết phòng đó
    @GetMapping("/view-room/{id}")
    public String roomDetails(@PathVariable int id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        model.addAttribute("reviews", reviewsService.getReviewsByRoomId(id));

        return "view_room";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    //lưu đăng kí
    @PostMapping("/saveRegister")
    public String saveRegister(@ModelAttribute UserDtls userDtls, HttpSession session) {

        boolean existsEmail = userService.existsEmail(userDtls.getEmail());
        if(existsEmail) {
            session.setAttribute("errorMsg", "Email already exists");
        }else{
            UserDtls saveUser = userService.saveUser(userDtls);
            if (!ObjectUtils.isEmpty(saveUser)) {
                session.setAttribute("sucMsg","register  success!");
            }else{
                session.setAttribute("sucMsg","register failed!");
            }
        }


        return "redirect:/register";
    }


    //người dungf thêm đánh giá
    @PostMapping("/save-reviews")
    public String saveReview(@ModelAttribute ReviewsRequest reviewsRequest, Principal p) throws Exception {

        UserDtls user = commonUtil.getLoggedInUserDetails(p);


        reviewsService.saveReviews(user.getId(),reviewsRequest);

        return "redirect:/view-room/" + reviewsRequest.getRoom().getId();
    }


//người dùng cập nhật đannh giá
    @PostMapping("/update-reviews")
    public String updateReview(@ModelAttribute ReviewsRequest reviewsRequest) throws Exception {

        reviewsService.updateReview(reviewsRequest);

        return "redirect:/view-room/" + reviewsRequest.getRoom().getId();
    }


    //xoá đánh giá
    @GetMapping("/delete-reviews")
    public String deleteReview(@RequestParam int id,@RequestParam int roomId)  {

        reviewsService.deleteReview(id);

        return "redirect:/view-room/" + roomId;
    }


    //hển thị trang liên hệ
    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }

    //lưu thng tin liên hệ
    @PostMapping("/saveContact")
    public String saveContact(@ModelAttribute Feedback feedback, HttpSession session) {


        Feedback saveFeedback = feedbackService.saveFeedback(feedback);
        if (!ObjectUtils.isEmpty(saveFeedback)) {
                session.setAttribute("sucMsg","Thanks for your feedback!");
            }else{
                session.setAttribute("errorMsg","Sending failed, please resend.");
            }



        return "redirect:/contact";
    }

    //hiển thị trang thông tin
    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

}
