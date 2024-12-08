package com.purna.studentteacherbookingpractice.Controllers;


import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("newuser", new Users());
        return "admin/home";

    }

    @GetMapping("/addteacher")
    public String addTeacher(Model model){
        model.addAttribute("newteacher",new Users());
        return "/admin/addteacher";

    }

    @PostMapping("/addteacher")
    public  String addNewTeacher(@ModelAttribute Users users, Model model){
        model.addAttribute("newteacher",new Users());
        userService.registerNewTeacher(users);
        return "/admin/addteacher";
    }
}
