package com.example.Booking_Hotel.controller;

import com.example.Booking_Hotel.model.BookingRoom;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.RoomType;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.repository.RoomTypeRepository;
import com.example.Booking_Hotel.service.*;
import com.example.Booking_Hotel.util.BookingStatus;
import com.example.Booking_Hotel.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;


    @Autowired
    private UserService userService;

    @Autowired
    private BookingRoomService bookingRoomService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    public CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @ModelAttribute
    public void getUserDDetails(Principal p, Model model) {
        if (p!=null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
        }
    }

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }


    //Hiển thị tata ca loại phòng có trong database lên view loại phòng
    @GetMapping("/roomType")
    public String roomType(Model model){
        model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
        return "admin/roomType";
    }



    //Lưu loại phòng
    @PostMapping("/saveRoomType")
    public String saveRoomType(@ModelAttribute RoomType roomType, HttpSession session){

        Boolean existsRoomType = roomTypeService.exitsRoomType(roomType.getName());
        if (existsRoomType){
            session.setAttribute("errorMsg","Exists Room Type Name!");
        }else{
            RoomType saveRoomType = roomTypeService.saveRoomType(roomType);
            if(!ObjectUtils.isEmpty(saveRoomType)){
                session.setAttribute("sucMsg","save success!");
            }else{
                session.setAttribute("errorMsg","save failed!");
            }
        }

        return "redirect:/admin/roomType";
    }


    //Hiển thị form edit loại phòng
    @GetMapping("/loadEditRoomType/{id}")
    public String loadEditRoomType(@PathVariable Integer id, Model model){
        model.addAttribute("roomType", roomTypeService.getRoomTypeById(id));
        return "admin/edit_RoomType";
    }


    //cập nhật loại phòng
    @PostMapping("/updateRoomType")
    public String updateRoomType(@ModelAttribute RoomType roomType, HttpSession session){

            RoomType oldRoomType = roomTypeService.getRoomTypeById(roomType.getId());
            if (!ObjectUtils.isEmpty(oldRoomType)) {
                oldRoomType.setName(roomType.getName());
                oldRoomType.setIsActive(roomType.getIsActive());
            }
            RoomType updateRoomType = roomTypeService.saveRoomType(oldRoomType);

            if (!ObjectUtils.isEmpty(updateRoomType)) {
                session.setAttribute("sucMsg", "save success!");
            } else {
                session.setAttribute("errorMsg", "save failed!");
            }



        return "redirect:/admin/loadEditRoomType/"+roomType.getId();
    }


    //xoá loại phòng
    @GetMapping("/deleteRoomType/{id}")
    public String deleteRoomType(@PathVariable("id") Integer id, HttpSession session){

        Boolean deleteRoomType = roomTypeService.deleteRoomType(id);

            if(deleteRoomType){
                session.setAttribute("sucMsg","delete success!");
            }else {
                session.setAttribute("errorMsg", "delete failed!");
            }

        return "redirect:/admin/roomType";
    }



    //hiển thị form thêm phòng
    @GetMapping("/loadAddRoom")
    public String addRoom(Model model){
        model.addAttribute("roomTypes", roomTypeService.getAllActiveRoomType());
        return "admin/add_Room";
    }


    //lưu phòng
    @PostMapping("/saveRoom")
    public String saveRoom(@ModelAttribute Room room,@RequestParam("file1") MultipartFile file,
                           @RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,
                           HttpSession session) throws IOException {
        Room saveRoom = roomService.saveRoom(room, file,file2,file3);

        if(!ObjectUtils.isEmpty(saveRoom)){
            session.setAttribute("errorMsg", "save room failed!");
        }else {

            session.setAttribute("sucMsg","save room success!");
        }
        return "redirect:/admin/loadAddRoom";
    }


    //hiển thị tất cả data phòng len view và phân trang
    @GetMapping("/loadRoom")
    public String loadRoom(Model model,@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                           @RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,String ch){
//        model.addAttribute("rooms",roomService.getAllRooms());

        Page<Room> roomPage =null;

        if (ch!=null && ch.length()>0){
            roomPage=roomService.searchRoomPagination(ch,pageNo,pageSize);
        }else{
            roomPage=roomService.getAllRoomPagination(pageNo,pageSize);
        }
      List<Room> rooms = roomPage.getContent();

        model.addAttribute("rooms",rooms);

        model.addAttribute("pageNo", roomPage.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements",roomPage.getTotalElements());
        model.addAttribute("totalPages", roomPage.getTotalPages());
        model.addAttribute("isFirst", roomPage.isFirst());
        model.addAttribute("isLast", roomPage.isLast());

        return "admin/room";
    }


    //Hiển thị form edit phòng
    @GetMapping("/loadEditRoom/{id}")
    public String loadEditRoom(@PathVariable Integer id, Model model){
        model.addAttribute("room",roomService.getRoomById(id));
        model.addAttribute("roomTypes", roomTypeService.getAllActiveRoomType());
        return "admin/edit_Room";
    }



    //cập nhật phòng
    @PostMapping("/updateRoom")
    public String updateRoom(@ModelAttribute Room room,@RequestParam("file1") MultipartFile file,
                           @RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,
                           HttpSession session) throws IOException {
        Room updateRoom = roomService.updateRoom(room, file,file2,file3);

        if(!ObjectUtils.isEmpty(updateRoom)){
            session.setAttribute("sucMsg","update room success!");
        }else {

            session.setAttribute("errorMsg", "update room failed!");
        }
        return "redirect:/admin/loadEditRoom/"+room.getId();
    }



    //Xoá phòng
    @GetMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable("id") Integer id, HttpSession session){

        Boolean deleteRoom = roomService.deleteRoom(id);

        if(deleteRoom){
            session.setAttribute("sucMsg","delete success!");
        }else {
            session.setAttribute("errorMsg", "delete failed!");
        }

        return "redirect:/admin/loadRoom";
    }



    //Hiển thị tất cả khách hàng user
    @GetMapping("/loadUser")
    public String getAllUser(Model model){
         model.addAttribute("users",userService.getAllUsers("ROLE_USER")) ;
        return "admin/users";
    }


    // Cập nhật status cho tài khoản user
    @GetMapping("/updateSts")
    public String updateAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,HttpSession session){
        Boolean b = userService.updateAccountStatus(status, id);
        if(b){
            session.setAttribute("sucMsg","update user success!");
        }else {

            session.setAttribute("errorMsg", "update user failed!");
        }

        return "redirect:/admin/loadUser";
    }


    //Hiển thị tất cả booking của khách hàng và phân trang
    @GetMapping("/booking")
    public String getAllBooking(Model model,@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                @RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,String ch){
//        model.addAttribute("bookings",bookingRoomService.getAllBookingRooms());

        Page<BookingRoom> bookingRoomPage=null;


        if (ch!=null && ch.length()>0){
            bookingRoomPage=bookingRoomService.searchBookingRoomPagination(ch,pageNo,pageSize);
        }else{
            bookingRoomPage= bookingRoomService.getAllBookingRoomsPagination(pageNo,pageSize);
        }

        List<BookingRoom> bookingRooms = bookingRoomPage.getContent();//lấy nội dung sản phẩm

        model.addAttribute("bookings",bookingRooms);

        model.addAttribute("pageNo", bookingRoomPage.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", bookingRoomPage.getTotalElements());
        model.addAttribute("totalPages", bookingRoomPage.getTotalPages());
        model.addAttribute("isFirst", bookingRoomPage.isFirst());
        model.addAttribute("isLast", bookingRoomPage.isLast());
        return "/admin/booking";
    }


//cập nhật trạng thái booking
    @GetMapping("/update-booking-status")
    public String updateBookingStatus(@RequestParam Integer id, @RequestParam Integer st,HttpSession session){
        BookingStatus[] values = BookingStatus.values();
        String status=null;
        for (BookingStatus bookingStatus : values) {
            if (bookingStatus.getId() == st) {
                status=bookingStatus.getName();
            }
        }
        BookingRoom updateBookingRoom = bookingRoomService.updateBookingStatus(id, status);

//        try {
//            commonUtil.sendEmailForProductOrder(updateBookingRoom,status);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        if(!ObjectUtils.isEmpty(updateBookingRoom)){
            session.setAttribute("sucMsg","Booking status updated successfully");
        }else{
            session.setAttribute("errorMsg","Booking status update failed");
        }

        return "redirect:/admin/booking";
    }




    //Hiển thị thống kê
    @GetMapping("/load-bookingStats")
    public String getBookingStats(Model model){
        model.addAttribute("totalRooms",roomService.totalRoom());
        model.addAttribute("totalBookings",bookingRoomService.countBookingRoomByStatus());
        model.addAttribute("totalBookingCancels",bookingRoomService.countBookingRoomCancelled());
        model.addAttribute("totalUsers",userService.countUser());
        model.addAttribute("totalRevenue",bookingRoomService.totalBookingRevenue());
        Integer currentYear= LocalDate.now().getYear();
        model.addAttribute("bookingCounts",bookingRoomService.getBookingsPerMonth(currentYear));
        model.addAttribute("totalRoomTypes",roomTypeService.getCountRoomType());
        model.addAttribute("totalReviews",reviewsService.getCountReviews());
        model.addAttribute("totalFeedbacks",feedbackService.getCountFeedbacks());
        return "/admin/bookingStats";
    }


    //Hiển thị giao diên thông tin cá nhân admin
    @GetMapping("/profile")
    public String Profile(){
        return "/admin/profile";
    }



    // Cập nhật profile của tài khoản user
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute UserDtls userDtls, MultipartFile img, HttpSession session){

        UserDtls userDtls1 = userService.updateProfileUser(userDtls, img);
        if (!ObjectUtils.isEmpty(userDtls1)) {
            session.setAttribute("sucMsg","update profile success!");
        }else {
            session.setAttribute("errorMsg", "update profile failed!");
        }
        return "redirect:/admin/profile";
    }


    //Thay đổi mật khâur
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


    //Hiển thị feedback
    @GetMapping("/loadFeedback")
    public String getFeedback(Model model){

        model.addAttribute("feedbacks",feedbackService.getAllFeedback());
        return "/admin/feedback";
    }


//xoá feedback
    @GetMapping("/deleteFeedback/{id}")
    public String deleteFeedback(@PathVariable("id") Integer id, HttpSession session){

        Boolean deleteFeedback = feedbackService.deleteFeedback(id);

        if(deleteFeedback){
            session.setAttribute("sucMsg","delete success!");
        }else {
            session.setAttribute("errorMsg", "delete failed!");
        }

        return "redirect:/admin/loadFeedback";
    }

    //xoá tất cả feedback
    @GetMapping("/deleteAllFeedback")
    public String deleteAllFeedback(){

        feedbackService.deleteAllFeedback();

        return "redirect:/admin/loadFeedback";
    }

    //hiển thị  tất cả đánh gá của tất cả khách hàng
    @GetMapping("/review")
    public String getAllReview(Model model){
        model.addAttribute("reviews", reviewsService.getAllReviews());


        return "/admin/reviews";
    }


    //xoá đánh giá khách hàng
    @GetMapping("/delete-reviews/{id}")
    public String deleteReview(@PathVariable int id)  {

        reviewsService.deleteReview(id);

        return "redirect:/admin/review";
    }

    //xoá tât cả đánh giá
    @GetMapping("/delete-allreviews")
    public String deleteAllReview()  {

       reviewsService.deleteAllReview();

        return "redirect:/admin/review";
    }

}
