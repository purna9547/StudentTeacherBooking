package com.purna.studentteacherbookingpractice.Models;

import lombok.Data;

@Data
public class ChatResponse {
    private String response;
    public ChatResponse(String responseText) {
        this.response = responseText;
    }

    public String getResponse() {  // ✅ Getter
        return response;
    }

    public void setResponse(String response) {  // ✅ Setter
        this.response = response;
    }
}
