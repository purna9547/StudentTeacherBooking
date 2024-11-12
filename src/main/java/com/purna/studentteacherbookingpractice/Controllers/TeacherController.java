package com.purna.studentteacherbookingpractice.Controllers;


import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.AppointmentService;
import com.purna.studentteacherbookingpractice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;
    @GetMapping("/home")
    public String Home(Model model){
        model.addAttribute("currentUser", userService.getAuthenticatedUser());
        return "teacher/home";
    }
    @GetMapping("/viewappointments")
    public String viewALlAppointment(Model model){
        UUID id=userService.getAuthenticatedUser().getId();

        List<Appointment> allAppointment=appointmentService.getAllAppointment(id);
        model.addAttribute("allAppointment",allAppointment);

        model.addAttribute("currentUser",userService.getAuthenticatedUser());
        model.addAttribute("upcomingAppointment",appointmentService.getUpcomingUser(id));
        System.out.println(appointmentService.getUpcomingUser(id));
        model.addAttribute("pastAppointment",appointmentService.getPastUser(id));
        System.out.println(appointmentService.getPastUser(id));
        return "teacher/viewappointments";
    }


    @GetMapping("/approve")
    public String approveAppointment(@RequestParam("id")UUID id){
        appointmentService.approve(id);
        return "redirect:/teacher/viewappointments";
    }

    @GetMapping("/reject")
    public String cancelAppointment(@RequestParam("id")UUID id){
        appointmentService.reject(id);
        return "redirect:/teacher/viewappointments";
    }


}
