package com.purna.studentteacherbookingpractice.Controllers;


import com.purna.studentteacherbookingpractice.Models.Mail;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.MailService;
import com.purna.studentteacherbookingpractice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("newuser", new Users());
        List<Users> teacherList=userService.getUserByRole("TEACHER");
        List<Users>pendinStudentList=userService.getPendingStudent();
        model.addAttribute("teacherList",teacherList);
        model.addAttribute("pendingStudentList",pendinStudentList);
        return "admin/home";

    }

    @GetMapping("/addteacher")
    public String addTeacher(Model model){
        model.addAttribute("newteacher",new Users());
        return "/admin/addteacher";

    }

    @PostMapping("/addteacher")
    public String addNewTeacher(@ModelAttribute Users users, Model model){
        model.addAttribute("newteacher",new Users());
        userService.registerNewTeacher(users);
        String msg = "<div style='font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;'>"
                + "<div style='background-color: #f7f7f7; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>"
                + "<h2 style='color: #12A0DCFF; text-align: center;'>Welcome, " + users.getFullname().split(" ")[0] + "!</h2>"
                + "<hr style='border: 0; border-top: 3px solid #12A0DCFF; margin: 20px 0;'>"
                + "<p style='font-size: 16px; color: #333;line-height: 1.5;'>You are added by our admin. We are excited to have you on board.</p>"
                + "<p style='font-size: 16px; line-height: 1.5;'>Your username is: <strong>" + users.getUserName() + "</strong></p>"
                + "<p style='font-size: 16px; color: #333;line-height: 1.5;'>Please use this username to log in.</p>"
                + "<p style='font-size: 16px; color: #333;line-height: 1.5; margin-top: 20px;'>Best regards,<br>Team Innovate</p>"
                + "<hr style='border: 0; border-top: 1px solid #ccc; margin: 20px 0;'>"
                + "<p style='font-size: 12px; color: #333; text-align: center;'>If you did not register for this account, please ignore this email.</p>"
                + "</div>"
                + "</div>";

        Mail mail = new Mail(
                users.getEmail(),
                "Welcome to MailSender",
                msg
        );
        try {
            mailService.sendRegistrationMail(mail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/admin/addteacher";
    }

    @GetMapping("/approve/{id}")
    public String approveStudents(@PathVariable UUID id){
        userService.approveStudent(id);
        return "redirect:/admin/home";
    }

    @GetMapping("/reject/{id}")
    public String rejectedStudents(@PathVariable UUID id){
        userService.rejectStudent(id);
        return "redirect:/admin/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable UUID id){
        userService.deleteTeacher(id);
        return "redirect:/admin/home";
    }

    @GetMapping("/updateTeacher")
    public String updateTeacher(@RequestParam("id") UUID id ,Model model){
        model.addAttribute("userInfo",userService.findById(id));
        return "/admin/updateTeacher";

    }
    @PostMapping("/updateTeacher")
    public String submitUpdateForm(@ModelAttribute("userInfo") Users users){
        System.out.println(users);
        UUID id=users.getId();
        userService.updateTeacher(id,users);
        return "redirect:/admin/home";
    }
}
