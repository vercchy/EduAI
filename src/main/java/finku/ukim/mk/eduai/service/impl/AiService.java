package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.service.interfaces.AIServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class AiService implements AIServiceInterface {

    private static final String API_URL = "https://api.fireworks.ai/inference/v1/chat/completions";

    @Value("${fireworks.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String evaluateStudentAnswer(String question, String answer, Float maximumPoints) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);


        HttpEntity<Map<String, Object>> entity = getMapHttpEntity(question, answer, maximumPoints, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, Map.class);
            Map<String, Object> result = (Map<String, Object>) ((List<?>) response.getBody().get("choices")).get(0);
            Map<String, String> message = (Map<String, String>) result.get("message");
            return message.get("content");
        } catch (Exception e) {
            return "Error while evaluating the answer: " + e.getMessage();
        }
    }

    private static HttpEntity<Map<String, Object>> getMapHttpEntity(String question, String answer, Float maximumPoints, HttpHeaders headers) {
        String prompt = "Question: " + question + "\nStudent Answer: " + answer;

        Map<String, Object> systemMsg = Map.of(
                "role", "system",
                "content", "You are an AI that evaluates student answers. Rate the answer from 1 to " + maximumPoints + ", and if it is incorrect or partially correct, explain in 3-4 sentences why. Return the score and explanation only."
        );

        Map<String, Object> userMsg = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = Map.of(
                "model", "accounts/fireworks/models/llama4-scout-instruct-basic",
                "messages", List.of(systemMsg, userMsg)
        );

        return new HttpEntity<>(body, headers);
    }
}
