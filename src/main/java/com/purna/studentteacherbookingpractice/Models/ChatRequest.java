package com.purna.studentteacherbookingpractice.Models;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    public String getMessage() {  // ✅ Add this method
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
