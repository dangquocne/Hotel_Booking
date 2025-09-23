package com.example.Booking_Hotel.repository;

import com.example.Booking_Hotel.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {
    public UserDtls findByEmail(String email);

    List<UserDtls> findByRole(String role);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM UserDtls u WHERE u.role='ROLE_USER'")
    Integer countUserDtls();


}
