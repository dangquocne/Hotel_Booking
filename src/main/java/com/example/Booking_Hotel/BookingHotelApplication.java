package com.example.Booking_Hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookingHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingHotelApplication.class, args);
	}

}
