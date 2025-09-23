package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.RoomType;
import com.example.Booking_Hotel.repository.RoomRepository;
import com.example.Booking_Hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room saveRoom(Room room,MultipartFile image,MultipartFile image2,MultipartFile image3) throws IOException {
        String imageName1 = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        String imageName2 = image2.isEmpty() ? "default.jpg" : image2.getOriginalFilename();
        String imageName3= image3.isEmpty() ? "default.jpg" : image3.getOriginalFilename();

        room.setImage1(imageName1);
        room.setImage2(imageName2);
        room.setImage3(imageName3);
        room.setDiscount(0);
        room.setDiscountPrice(room.getPrice());

        Room saveRoom = roomRepository.save(room);
        if(!ObjectUtils.isEmpty(saveRoom)){

            //Luu vao duong dan hinh anh trong O D:/ sau khi nhan luu thong tin
            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image.getOriginalFilename());
            Path path2 = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image2.getOriginalFilename());
            Path path3 = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image3.getOriginalFilename());
            //            System.out.println(path);

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(image2.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(image3.getInputStream(), path3, StandardCopyOption.REPLACE_EXISTING);
        }
        return null;
    }

    @Override
    public Room updateRoom(Room room,MultipartFile image,MultipartFile image2,MultipartFile image3) {
        Room oldRoom = getRoomById(room.getId());

        String imageName1 = image.isEmpty() ? oldRoom.getImage1() : image.getOriginalFilename();
        String imageName2 = image2.isEmpty() ? oldRoom.getImage2() : image2.getOriginalFilename();
        String imageName3= image3.isEmpty() ? oldRoom.getImage3() : image3.getOriginalFilename();

        //5=100*(5/100) 100-5=95
        double discount = room.getPrice()*(room.getDiscount()/100.0);
        double discountPrice = room.getPrice()-discount;

        oldRoom.setTitle(room.getTitle());
        oldRoom.setDescription(room.getDescription());
        oldRoom.setRoomType(room.getRoomType());
        oldRoom.setPrice(room.getPrice());
        oldRoom.setRoomNumber(room.getRoomNumber());
        oldRoom.setDiscount(room.getDiscount());
        oldRoom.setDiscountPrice(discountPrice);
        oldRoom.setIsActive(room.getIsActive());
        oldRoom.setImage1(imageName1);
        oldRoom.setImage2(imageName2);
        oldRoom.setImage3(imageName3);

        Room updateRoom = roomRepository.save(oldRoom);
        if(!ObjectUtils.isEmpty(updateRoom)){
            if (!image.isEmpty() || !image2.isEmpty() || !image3.isEmpty()){
                try{
                    //Luu vao duong dan hinh anh trong O D:/ sau khi nhan luu thong tin
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image.getOriginalFilename());
                    Path path2 = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image2.getOriginalFilename());
                    Path path3 = Paths.get(saveFile.getAbsolutePath() + File.separator + "room_img" + File.separator + image3.getOriginalFilename());
                    //            System.out.println(path);

                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(image2.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(image3.getInputStream(), path3, StandardCopyOption.REPLACE_EXISTING);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return room;
        }

        return null;
    }

    @Override
    public Room getRoomById(int id) {
        Room room = roomRepository.findById(id).orElse(null);
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }


    @Override
    public Page<Room> getAllRoomPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return roomRepository.findAll(pageable);
    }

    @Override
    public List<Room> getAllActiveRoom(String roomType) {
        List<Room> roomList =null;

        if (!ObjectUtils.isEmpty(roomType)) {
            roomList = roomRepository.findByRoomType(roomType);
        } else{
            roomList = roomRepository.findByIsActiveTrue();

        }

        return roomList;
    }

    @Override
    public Boolean deleteRoom(int id) {
        Room room = roomRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(room)){
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    @Override
    public List<Room> getRoomByRoomType(String roomType) {
        List<Room> rooms=roomRepository.findByRoomType(roomType);
        return rooms;
    }

    @Override
    public Page<Room> getAllActiveRoomPagination(Integer pageNo, Integer pageSize,String roomType) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Room> roomPage =null;
        if (!ObjectUtils.isEmpty(roomType)) {
            roomPage = roomRepository.findByRoomType(pageable,roomType);
        } else{
            roomPage = roomRepository.findByIsActiveTrue(pageable);

        }
        return roomPage;
    }


    @Override
    public Page<Room> searchRoomPagination(String ch, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return roomRepository.searchRoomBy(ch,pageable);
    }

    @Override
    public Integer totalRoom() {
        return roomRepository.totalRoomNumber();
    }
}
