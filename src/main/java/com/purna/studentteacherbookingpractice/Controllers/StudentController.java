package com.purna.studentteacherbookingpractice.Controllers;

import com.purna.studentteacherbookingpractice.Configuration.SecurityServices;
import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.AppointmentService;
import com.purna.studentteacherbookingpractice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;
    @Autowired
    private SecurityServices securityServices;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView home(Model model) {
        String viewName="student/home";
        Map<String ,Object> mp=new HashMap<>();

        mp.put("currentUser",userService.getAuthenticatedUser());
        return new ModelAndView(viewName,mp);
    }
    @GetMapping("/editprofile")
    public String getEditForm(@RequestParam("id") UUID id, Model model) {
//        System.out.println(id);
        if (!securityServices.canEditProfile(id)) {
            return "redirect:/student/home";
        }
//        System.out.println("1");
        Users editUser = userService.findById(id);
        //System.out.println(editUser);
        model.addAttribute("userInfo", editUser);
        model.addAttribute("currentUser", userService.getAuthenticatedUser());
//        System.out.println("2");
        return "student/editprofile";

    }

    @PostMapping("/editprofile")
    public String submitEditForm(@ModelAttribute("userInfo") Users users,
                                 @RequestParam("profilePhoto") MultipartFile profilePhoto,
                                 Model model) {
        if (!profilePhoto.isEmpty()) {
            try {
                // Define upload directory
                String uploadDir = "uploads/profile_photos/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs(); // Create directory if it doesn't exist
                }

                // Validate file type
                String contentType = profilePhoto.getContentType();
                if (!contentType.startsWith("image/")) {
                    model.addAttribute("message", "Invalid file type. Only image files are allowed.");
                    return "student/editprofile";
                }

                // Save the file
                String fileName = users.getId() + "_" + profilePhoto.getOriginalFilename();
                File uploadFile = new File(uploadDir + fileName);
                profilePhoto.transferTo(uploadFile);

                // Update user profile photo path
                users.setProfilePhotoPath("/" + uploadDir + fileName); // Use relative path for front-end
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "File upload failed: " + e.getMessage());
                return "student/editprofile";
            }
        }

        // Save the updated user information
        userService.updateStudent(users.getId(), users);
        return "redirect:/student/home";
    }



    @GetMapping("/bookappoinment")
    public String bookAppoinmnt(Model model){
//        System.out.println(("121"));
        model.addAttribute("teacherList",userService.getUserByRole("TEACHER"));
        model.addAttribute("currentUser",userService.getAuthenticatedUser());
        model.addAttribute("appointmentDetails",new Appointment());
        return "student/bookappoinment";
    }

    @PostMapping("/bookappoinment")
    public String submitAppointmentForm(@ModelAttribute("appointmentDetails") Appointment appointment) throws Exception {
        appointmentService.saveAppointment(appointment);
        return "redirect:/student/bookappoinment";
    }

    @GetMapping("/courses")
    public String viewCourses(){
        return "student/courses";
    }

}

