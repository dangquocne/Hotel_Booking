package com.example.Booking_Hotel.controller;

import com.example.Booking_Hotel.dto.CreateMomoResponse;
import com.example.Booking_Hotel.dto.ReviewsRequest;
import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.dto.BookingRequest;
import com.example.Booking_Hotel.dto.BookingRoomRequest;
import com.example.Booking_Hotel.model.BookingRoom;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.service.*;
import com.example.Booking_Hotel.service.impl.MomoService;
import com.example.Booking_Hotel.util.BookingStatus;
import com.example.Booking_Hotel.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BookingListService bookingListService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRoomService bookingRoomService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private MomoService momoService;

    //lấy thông tin người dùng
    @ModelAttribute
    public void getUserDDetails(Principal p, Model model) {
        if (p!=null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
        }
    }

    //trang đăng nhập
    @GetMapping("/")
    public String login() {
        return "user/home";
    }



//    @GetMapping("/loadBookingList")
//    public String bookingList(){
//        return "/user/bookingList";
//    }

    //Thêm phòng vào trong danh sách khi chọn nhiều
    @GetMapping("/addBookingList")
    public String addBookingList(@RequestParam Integer rid,@RequestParam Integer uid, HttpSession session){

        BookingList bookingList = bookingListService.saveBookingList(rid, uid);
        if (!ObjectUtils.isEmpty(bookingList)) {
            session.setAttribute("sucMsg","add BookingList success!");
        }else {
            session.setAttribute("errorMsg", "add BookingList  failed!");
        }
        return "redirect:/view-room/"+rid;
    }


    //hiển thị tất cả data bookingList lên trang view bookingList.html
    @GetMapping("/bookingList")
    public String getBookingList(Principal p,Model model){
        UserDtls user = commonUtil.getLoggedInUserDetails(p);
        List<BookingList> bookingLists = bookingListService.getBookingListByUser(user.getId());
        model.addAttribute("bookingLists", bookingLists);

        if(bookingLists.size()>0) {
            Double totalBookedPrice = bookingLists.get(bookingLists.size() - 1).getTotalBookedPrice();
            model.addAttribute("totalBookedPrice", totalBookedPrice);
        }

        return "/user/bookingList";
    }


//    private UserDtls getLoggedInUserDetails(Principal p){
//        String email = p.getName();
//        UserDtls userDtls = userService.getUserByEmail(email);
//        return userDtls;
//    }


    //cập nhật tăng số lượng phòng muốn đặt được lưu trong trang bookingList
    @GetMapping("/booksQuantityUpdate")
    public String updateQuantity(@RequestParam String sy, @RequestParam Integer bid){
        bookingListService.updateQuantity(sy, bid);
        return "redirect:/user/bookingList";
    }


    //xoá tất cả lựa chọn khi có nhều phòng được thêm vào trang bookingList
    @GetMapping("/deleteAll")
    public String deleteAll(Principal p){
        UserDtls userDtls = commonUtil.getLoggedInUserDetails(p);
        bookingListService.deleteAllByUserId(userDtls.getId());
        return "redirect:/user/bookingList";
    }



    //hiển thị thông tin phòng cần đặt lên trang bookingRoom sau khi bấm nút book now
    @GetMapping("/loadBookingRoom/{id}")
    public String loadBookingRoom(@PathVariable Integer id,Model model){
        model.addAttribute("bookrooms", roomService.getRoomById(id));
        return "/user/bookingRoom";
    }


    //lhiển thị thông tin cần đặt lên trang bookingRoom_List sau khi bấm book now
    @GetMapping("/loadBookingRoom-List")
    public String loadBookingRoomList(){
        return "/user/bookingRoom_List";
    }



    //lưu thông tin đặt phòng khi đặt 1 phòng
    @PostMapping("/save-bookingRoom")
    public String saveBookingRoom(@ModelAttribute BookingRoomRequest bookingRoomRequest, @ModelAttribute BookingRequest request, Principal p, HttpSession session) throws Exception {


        //kiểm tra các input khi chưa nhập
        if(bookingRoomRequest.getCheckIn()==null || bookingRoomRequest.getCheckOut()==null ||
               bookingRoomRequest.getTotalDay()==null|| bookingRoomRequest.getPaymentType()==""||request.getEmail()=="" ||request.getName()=="" || request.getMobileNumber()=="") {
            session.setAttribute("errorMsg", "Please fill out the form!");
        }
        //kiểm tra ngày checkOut không được đến trc ngày checkIn, bắt buộc  checkOut phải đến sau
        else if (!bookingRoomRequest.getCheckOut().isAfter(bookingRoomRequest.getCheckIn())) {
            session.setAttribute("errorMsg", "Check-In date must come before check-out date");

        }

        else {
            UserDtls user = commonUtil.getLoggedInUserDetails(p);
//            bookingRoomService.saveBookingRoom(user.getId(), bookingRoomRequest, request);

            BookingRoom save=null;

            //kiểm tra thanh toán
            if (save == null) {
                String paymentType = bookingRoomRequest.getPaymentType(); // "COD" hoặc "ONLINE"
                if ("COD".equalsIgnoreCase(paymentType)) {
                    save=bookingRoomService.saveBookingRoom(user.getId(), bookingRoomRequest, request);

                    // Trả về trang success (tiền mặt)
                    return "/user/success";

                } else if ("ONLINE".equalsIgnoreCase(paymentType)) {

                   save = bookingRoomService.saveBookingRoom(user.getId(), bookingRoomRequest, request);

                    // Chuyển hướng sang trang thanh toán online (VNPAY, MOMO, ...)
//                return "/user/payment_online";
                    CreateMomoResponse momoResponse = momoService.createQR(save);
                    return "redirect:" + momoResponse.getPayUrl();

                }
            }
//            session.setAttribute("sucMsg", "field empty success!");
        }
        return "redirect:/user/loadBookingRoom/"+bookingRoomRequest.getRoom().getId();

    }



    //lưu thông tin đặt phòng khi muốn đặt nhiều phòng trong trang  BookingList
    @PostMapping("/save-bookingRoomForList")
    public String saveBookingRoomForBookingList(@ModelAttribute BookingRoomRequest bookingRoomRequest, @ModelAttribute BookingRequest request, Principal p) throws Exception {

        UserDtls user = commonUtil.getLoggedInUserDetails(p);


        bookingRoomService.saveBookingRoomForBookingList(user.getId(), bookingRoomRequest, request);



            String paymentType = bookingRoomRequest.getPaymentType(); // "COD" hoặc "ONLINE"
            if ("COD".equalsIgnoreCase(paymentType)) {

                //sau khi thanh toán thành công thì xoá giỏ hàng chứa phòng
                bookingListService.deleteAllByUserId(user.getId());

                // Trả về trang success (tiền mặt)
                return "/user/success";

            } else if ("ONLINE".equalsIgnoreCase(paymentType)) {

                //sau khi thanh toán thành công thì xoá giỏ hàng chứa phòng
                bookingListService.deleteAllByUserId(user.getId());

                // Chuyển hướng sang trang thanh toán online (VNPAY, MOMO, ...)
                return "/user/payment_online";
            }


        return "redirect:/user/loadBookingRoom-List";
    }



 //Hiển thị thông tin đặt phòng lên trang lịch sử my_booking khi khách đã đặt
    @GetMapping("/user-booking")
    public String myBooking(Model model,Principal p){
        UserDtls loggedInUser = commonUtil.getLoggedInUserDetails(p);
        List<BookingRoom> bookingRoomsByUser = bookingRoomService.getBookingRoomsByUser(loggedInUser.getId());
        model.addAttribute("bookings",bookingRoomsByUser);
        return "/user/my_booking";
    }


    //cập nhật trạng thái đặt phòng của người dùng
    @GetMapping("/update-status")
    public String updateBookingStatus(@RequestParam Integer id, @RequestParam Integer st,HttpSession session){
        BookingStatus[] values = BookingStatus.values();
        String status=null;
        for (BookingStatus bookingStatus : values) {
            if (bookingStatus.getId() == st) {
                status=bookingStatus.getName();
            }
        }
        BookingRoom updateBookingRoom = bookingRoomService.updateBookingStatus(id, status);
        try {
            commonUtil.sendEmailForProductOrder(updateBookingRoom,status);
        } catch (Exception e) {
           e.printStackTrace();
        }

        if(!ObjectUtils.isEmpty(updateBookingRoom)){
            session.setAttribute("sucMsg","Booking status updated successfully");
        }else{
            session.setAttribute("errorMsg","Booking status update failed");
        }

        return "redirect:/user/user-booking";
    }


    //hiển thị qr momo lên trang user/qr_momo
    @GetMapping("/qr-momo")
    public String qrMoMo(){
        return "/user/qr_momo";
    }





    //hiể thị profile lên user/profile
    @GetMapping("/profile")
    public String profile(){
        return "/user/profile";
    }


    //câp nhật profile của user
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute UserDtls userDtls, MultipartFile img, HttpSession session){

        UserDtls userDtls1 = userService.updateProfileUser(userDtls, img);
        if (!ObjectUtils.isEmpty(userDtls1)) {
            session.setAttribute("sucMsg","update profile success!");
        }else {
            session.setAttribute("errorMsg", "update profile failed!");
        }
        return "redirect:/user/profile";
    }


    //thay đổi mật khẩu user
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword,Principal p,HttpSession session){
        UserDtls loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

       if (matches){
           loggedInUserDetails.setPassword(passwordEncoder.encode(newPassword));
           UserDtls userDtls = userService.updateUser(loggedInUserDetails);
           if (ObjectUtils.isEmpty(userDtls)) {
               session.setAttribute("errorMsg","PassWord not update || Error in server");
           }else{
               session.setAttribute("sucMsg","Password updated successfully");
           }

       }else{
           session.setAttribute("errorMsg","Current PassWord incorrect");
       }


        return "redirect:/user/profile";
    }



}
