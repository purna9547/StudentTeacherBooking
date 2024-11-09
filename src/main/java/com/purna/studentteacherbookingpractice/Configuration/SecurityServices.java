package com.purna.studentteacherbookingpractice.Configuration;


import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Repositries.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServices {
    @Autowired
    private UserRepo userRepo;
    public boolean canEditProfile(UUID id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String currentUserName=authentication.getName();
        Users userToEdit=userRepo.findById(id).orElse(null);
        return userToEdit!=null && userToEdit.getUserName().equals(currentUserName);

    }
}
