package com.purna.studentteacherbookingpractice.Repositries;

import com.purna.studentteacherbookingpractice.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<Users, UUID> {
    Users findByUserName(String username) ;
    Users findByEmailOrUserName(String email, String userName);
    Users findByEmail(String email);
}
