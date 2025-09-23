package com.example.Booking_Hotel.service;

import com.example.Booking_Hotel.model.RoomType;

import java.util.List;

public interface RoomTypeService {

    public RoomType saveRoomType(RoomType roomType);

//    public RoomType updateRoomType(RoomType roomType);

    public Boolean exitsRoomType(String name);

    public List<RoomType> getAllRoomTypes();

    public List<RoomType> getAllActiveRoomType();

    public RoomType getRoomTypeById(int id);

    public Boolean deleteRoomType(int id);

    public Integer getCountRoomType();
}
