package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.model.BookingList;
import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.UserDtls;
import com.example.Booking_Hotel.repository.BookingListRepository;
import com.example.Booking_Hotel.repository.RoomRepository;
import com.example.Booking_Hotel.repository.UserRepository;
import com.example.Booking_Hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDtls saveUser(UserDtls userDtls) {
        userDtls.setIsEnable(true);
        userDtls.setProfileImage(null);
        userDtls.setRole("ROLE_USER");
        String encodedPassword = passwordEncoder.encode(userDtls.getPassword());
        userDtls.setPassword(encodedPassword);
        return userRepository.save(userDtls);
    }

    @Override
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        UserDtls userDtls = userRepository.findByEmail(email);
        return userDtls;
    }

    @Override
    public List<UserDtls> getAllUsers(String role) {
        return userRepository.findByRole(role);
    }


    @Override
    public Boolean updateAccountStatus(Boolean status, Integer id) {
        Optional<UserDtls> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            userById.get().setIsEnable(status);
            userRepository.save(userById.get());
            return true;
        }
        return false;
    }

    @Override
    public UserDtls getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public UserDtls updateProfileUser(UserDtls userDtls, MultipartFile image) {
        UserDtls oldUserDtls = getUserById(userDtls.getId());
        String imageName = image.isEmpty() ? oldUserDtls.getProfileImage() : image.getOriginalFilename();

        oldUserDtls.setProfileImage(imageName);
        oldUserDtls.setName(userDtls.getName());
        oldUserDtls.setMobileNumber(userDtls.getMobileNumber());
        oldUserDtls.setAddress(userDtls.getAddress());
        oldUserDtls.setCity(userDtls.getCity());

        UserDtls saveUser = userRepository.save(oldUserDtls);
        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!image.isEmpty()){
                try{
                    //Luu vao duong dan hinh anh trong O D:/ sau khi nhan luu thong tin
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + image.getOriginalFilename());

                    //            System.out.println(path);

                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return userDtls;
        }

        return null;
    }

    @Override
    public UserDtls updateUser(UserDtls userDtls) {
        return userRepository.save(userDtls);
    }

    @Override
    public Integer countUser() {
        return userRepository.countUserDtls();
    }
}
