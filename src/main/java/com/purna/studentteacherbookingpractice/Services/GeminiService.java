package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Models.ChatRequest;
import com.purna.studentteacherbookingpractice.Models.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GeminiService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChatResponse getGeminiResponse(ChatRequest request) {
        String url = geminiApiUrl + "?key=" + geminiApiKey;

        // Create request payload in correct format
        System.out.println("1");
        String requestBody = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(request.getMessage());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("2");

        // Create HTTP request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send request to Gemini API
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        System.out.println("3");
        System.out.println(response);


        // Extract response message safely
        String responseText = "No response";

        if (response.getBody() != null && response.getBody().containsKey("candidates")) {
            String input= response.getBody().get("candidates").toString();
            Pattern pattern = Pattern.compile("text=([^}]*)");
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                System.out.println(matcher.group(1)); // Output: Hi there! How can I help you today?
                responseText=matcher.group(1);
            };
        }
        System.out.println("4");
        System.out.println(responseText);
        return new ChatResponse(responseText);
    }
}
