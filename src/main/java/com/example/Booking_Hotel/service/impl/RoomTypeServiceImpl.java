package com.example.Booking_Hotel.service.impl;

import com.example.Booking_Hotel.model.RoomType;
import com.example.Booking_Hotel.repository.RoomTypeRepository;
import com.example.Booking_Hotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Override
    public RoomType saveRoomType(RoomType roomType) {

        return roomTypeRepository.save(roomType);
    }


    @Override
    public Boolean exitsRoomType(String name) {
        return roomTypeRepository.existsByName(name);
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public List<RoomType> getAllActiveRoomType() {
        List<RoomType> roomList =roomTypeRepository.findByIsActiveTrue();
        return roomList;
    }

    @Override
    public RoomType getRoomTypeById(int id) {
        RoomType roomType1 = roomTypeRepository.findById(id).orElse(null);
        return roomType1;
    }

    @Override
    public Boolean deleteRoomType(int id) {
        RoomType roomType = roomTypeRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(roomType)) {
            roomTypeRepository.delete(roomType);
            return true;
        }
        return false;
    }

    @Override
    public Integer getCountRoomType() {
        return roomTypeRepository.countRoomTypes();
    }
}
