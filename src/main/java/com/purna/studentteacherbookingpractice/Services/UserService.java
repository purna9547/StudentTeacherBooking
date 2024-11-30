package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Enum.UserRole;
import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Mail;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Repositries.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Autowired
    public MailService mailService;

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


    public void getGenerateOtp(String email, boolean b) throws Exception {
        Users users=userRepo.findByEmail(email);
        Random random=new Random();
        int otp=random.nextInt(1000,10000);
        users.setOtp(otp);

        if(!b){
            String msgBody = "<div style='font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;'>"
                    + "<div style='background-color: #f7f7f7; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>"
                    + "<h2 style='color: #12A0DCFF; text-align: center;'>Password Reset Request</h2>"
                    + "<hr style='border: 0; border-top: 3px solid #12A0DCFF; margin: 20px 0;'>"
                    + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>Dear " + users.getFullname().split(" ")[0] + ",</p>"
                    + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>We received a request to reset your password. Please use the following OTP to reset your password:</p>"
                    + "<p style='font-size: 20px; color: #333; text-align: center;'><strong>" + users.getOtp() + "</strong></p>"
                    + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>If you did not request a password reset, please ignore this email.</p>"
                    + "<p style='font-size: 16px; color: #333; line-height: 1.5; margin-top: 20px;'>Best regards,<br>Team Innovate</p>"
                    + "<hr style='border: 0; border-top: 1px solid #ccc; margin: 20px 0;'>"
                    + "<p style='font-size: 12px; color: #333; text-align: center;'>If you did not request this password reset, please disregard this email.</p>"
                    + "</div>"
                    + "</div>";
            Mail mail= new Mail(users.getEmail(),
                    "Forgot Password",
                    msgBody);
            mailService.sendRegistrationMail(mail);

        }
        userRepo.save(users);
    }

    public boolean validateOtp(String email, int otp, String newPassword) throws Exception {
        Users user=userRepo.findByEmail(email);
        System.out.println(newPassword);
        int databaseOtp=user.getOtp()   ;
        if (databaseOtp==otp){
            System.out.println(newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            getGenerateOtp(email,true);
            return true;
        }
        getGenerateOtp(email,true);
        return false;
    }
}
