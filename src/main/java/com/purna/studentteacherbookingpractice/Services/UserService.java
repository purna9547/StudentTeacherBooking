package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Enum.UserRole;
import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Repositries.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    public UserRepo userRepo;

//    public Users findByUserName(String userName) {
//        return userRepo.findByUserName(userName);
//    }

    public Users findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerNewUser(Users users) {

        String name=users.getFullname();
        String sname=name.toLowerCase();
        String fName=sname.split(" ")[0];
        Random random= new Random();
        int rand=random.nextInt(1000,10000);
        String userName=fName+rand;
        users.setUserName(userName);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRole(String.valueOf(UserRole.STUDENT));
        return userRepo.save(users);
    }

    public Users getAuthenticatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String currentUsername=authentication.getName();
        return findByUsername(currentUsername);
    }

    private Users findByUsername(String currentUsername) {
        return userRepo.findByUserName(currentUsername);
    }

    public Users findById(UUID id) {
        return userRepo.findById(id).get();
    }
    public void updateStudent(UUID id,Users users){
        Users updatestudent=findById(id);
        updatestudent.setFullname(users.getFullname());
        updatestudent.setEmail(users.getEmail());
        userRepo.save(updatestudent);
    }

    public List<Users> getUserByRole(String role) {
        List<Users> alluser=userRepo.findAll();
        List<Users>result=new ArrayList<>();
        for ( Users i:alluser){
//            System.out.println(i);
            if(i.getRole()!=null && i.getRole().equals(role)){
                result.add(i);
            }
        }
//        System.out.println(result);
        return result;
    }


}
