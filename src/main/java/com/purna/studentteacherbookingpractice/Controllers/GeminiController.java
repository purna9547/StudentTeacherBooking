package com.purna.studentteacherbookingpractice.Controllers;

import com.purna.studentteacherbookingpractice.Models.ChatRequest;
import com.purna.studentteacherbookingpractice.Models.ChatResponse;
import com.purna.studentteacherbookingpractice.Services.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class GeminiController {
    @Autowired
    private GeminiService geminiService;
    @GetMapping("/chat")
    public String chatBot(Model model){
//        model.addAttribute("newUser");
        return "student/chat";
    }
    @PostMapping("/chatapi")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        System.out.println("Hello");
        return new ResponseEntity<>(geminiService.getGeminiResponse(chatRequest), HttpStatus.OK);
    }

}
