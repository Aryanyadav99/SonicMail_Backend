package com.XLR8.email_helper.Service;

import com.XLR8.email_helper.Entity.Email;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper mapper;

    public EmailService(WebClient.Builder webClientBuilder,
                        @Value("${perplexity.api.url}") String baseUrl,
                        @Value("${perplexity.api.key}") String perplexityapiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = perplexityapiKey;
        this.mapper = new ObjectMapper();
    }

    public Mono<String> generateEmailReply(Email email) {
        String prompt = buildPrompt(email);

        String requestBody = String.format("""
            {
              "model": "sonar",
              "messages": [
                {"role": "user", "content": "%s"}
              ]
            }
            """, escapeJson(prompt));

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractResponseContent);
    }

    private String extractResponseContent(String response) {
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode choices = rootNode.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0).path("message").path("content").asText();
            }
            return "No valid response from API";
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String buildPrompt(Email email) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply to the following email without asking any questions: ");
        if (email.getTone() != null && !email.getTone().isEmpty()) {
            prompt.append("Use a ").append(email.getTone()).append(" tone. ");
        }
        prompt.append("Original Email:\n").append(email.getEmailContent());
        return prompt.toString();
    }

    // Simple JSON string escape to handle quotes and special characters in prompt
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
