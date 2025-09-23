package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.model.Room;
import com.example.Booking_Hotel.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    public Room saveRoom(Room room, MultipartFile file,MultipartFile file2,MultipartFile file3) throws IOException;

    public Room updateRoom(Room room,MultipartFile image,MultipartFile image2,MultipartFile image3);

    public Room getRoomById(int id);

    public List<Room> getAllRooms();


    public Boolean deleteRoom(int id);

    public List<Room> getRoomByRoomType(String roomType);

    public List<Room> getAllActiveRoom(String roomType);

    public Page<Room> getAllActiveRoomPagination(Integer pageNo, Integer pageSize,String roomType);

    public Page<Room> getAllRoomPagination(Integer pageNo, Integer pageSize);

    public Page<Room> searchRoomPagination(String ch, Integer pageNo, Integer pageSize);

    public Integer totalRoom();
}
