package com.purna.studentteacherbookingpractice.Controllers;

import com.purna.studentteacherbookingpractice.Dto.UserDto;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    public UserService userService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("newUser", new Users());
        return "home";
    }
    @GetMapping("/signup")
    public String getRegistration(Model model){
        model.addAttribute("newUser", new Users());
        return "signup";
    }
    @PostMapping("/signup")
        public ModelAndView getRegister(@ModelAttribute Users users, UserDto userdto,Model model){
        Map<String, Users> mp=new HashMap<>();
        mp.put("newuser",new Users());
        Users users2=userService.findByEmail(userdto.getEmail());
        if(users2!=null){
            model.addAttribute("Email exist",users2);
            return new ModelAndView(new RedirectView("/signup"));
        }

        userService.registerNewUser(users);
        return new ModelAndView(new RedirectView("/login"));
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("newUser",new Users());
        return "login";
    }
}
