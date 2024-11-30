package com.purna.studentteacherbookingpractice.Controllers;

import com.purna.studentteacherbookingpractice.Dto.UserDto;
import com.purna.studentteacherbookingpractice.Models.Mail;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Services.MailService;
import com.purna.studentteacherbookingpractice.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.HashMap;

@Controller
//@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserService userService;
    @Autowired
    private MailService mailService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newUser", new Users());
        return "home";
    }

    @GetMapping("/signup")
    public String getRegistration(Model model) {
        model.addAttribute("newUser", new Users());
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView getRegister(@ModelAttribute Users users, UserDto userdto, Model model) {
        Map<String, Users> mp = new HashMap<>();
        mp.put("newuser", new Users());
        Users users2 = userService.findByEmail(userdto.getEmail());
        if (users2 != null) {
            model.addAttribute("Email exist", users2);
            return new ModelAndView(new RedirectView("/signup"));
        }

        userService.registerNewUser(users);
        String msg = "<div style='font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;'>"
                + "<div style='background-color: #f7f7f7; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>"
                + "<h2 style='color: #12A0DCFF; text-align: center;'>Welcome, " + users.getFullname().split(" ")[0] + "!</h2>"
                + "<hr style='border: 0; border-top: 3px solid #12A0DCFF; margin: 20px 0;'>"
                + "<p style='font-size: 16px; color: #333;line-height: 1.5;'>Thank you for registering with Us. We are excited to have you on board.</p>"
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
        return new ModelAndView(new RedirectView("/login"));
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("newUser", new Users());
        return "login";
    }

    @GetMapping("/forget")
    public String frogetPassword(Model model) {
        model.addAttribute("forgotPass", new Users());
        return "forget";
    }

    @PostMapping("forget")
    public String getForgetPass(@ModelAttribute("forgotPass") Users users, HttpSession session, Model model) throws Exception {
        String email = users.getEmail();
        session.setAttribute("forgotEmail", email);
        userService.getGenerateOtp(email, false);
        return "redirect:/otp";

    }

    @GetMapping("/otp")
    public String getOtp(Model model) {
        model.addAttribute("forgotPass", new Users());
        return "otp";
    }

    @PostMapping("/otp")
    public String getValidOtp(@ModelAttribute("forgotPass") Users users, HttpSession session, Model model) throws Exception {
        System.out.println("21");
        String email = (String) session.getAttribute("forgotEmail");
        session.removeAttribute("forgotEmail");
        int otp = users.getOtp();
        System.out.println("121");
        String newPassword = users.getPassword();
        System.out.println(newPassword);
        boolean isChanged = userService.validateOtp(email, otp, newPassword);
        if (isChanged) {
            model.addAttribute("message", "Password changed successfully!");
        } else {
            model.addAttribute("message", "Failed to change password. Please try again.");
        }
        return "redirect:/login";
    }
}
