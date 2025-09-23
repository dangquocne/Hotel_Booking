package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.model.UserDtls;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public UserDtls saveUser(UserDtls userDtls);

    UserDtls getUserByEmail(String email);

    public List<UserDtls> getAllUsers(String role);

    public Boolean updateAccountStatus(Boolean status, Integer id);

    public UserDtls getUserById(Integer id);

    public UserDtls updateProfileUser(UserDtls userDtls, MultipartFile img);

    public UserDtls updateUser(UserDtls userDtls);

    public boolean existsEmail(String email);

    public Integer countUser();

}
