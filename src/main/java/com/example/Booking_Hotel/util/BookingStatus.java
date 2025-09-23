package com.example.Booking_Hotel.util;

public enum BookingStatus {

    IN_PROGRESS(1, "In Progress"),         // Mới đặt, đang xử lý
    APPROVED(2, "Approved"),               // Đã duyệt
    CANCELLED(3, "Cancelled"),             // Đã huỷ
    COMPLETED(4, "Completed");  // Đã hoàn tất (trả phòng, thanh toán xong)
    private int id;

    private String name;

    BookingStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
