package org.example.loganalyzer.service;

import org.example.loganalyzer.dto.AnalyzeLogsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    public AiClient(WebClient.Builder builder,
                    @Value("${openai.api.base-url}") String baseUrl,
                    ObjectMapper objectMapper) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
        this.objectMapper = objectMapper;
    }

    public AnalyzeLogsResponse analyzeLogs(String prompt) {
        try {
            // Build OpenAI Chat Completions style request payload
            String requestBody = """
                    {
                      "model": "%s",
                      "messages": [
                        {
                          "role": "user",
                          "content": %s
                        }
                      ],
                      "temperature": 0.1
                    }
                    """.formatted(model, toJsonString(prompt));

            String rawResponse = webClient.post()
                    .uri("/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(ex -> {
                        ex.printStackTrace();
                        return Mono.just("{}");
                    })
                    .block();

            if (rawResponse == null || rawResponse.isEmpty()) {
                return emptyFallback("Empty response from AI provider");
            }

            // Extract the assistant content (should be JSON string)
            String content = extractContentFromResponse(rawResponse);
            if (content == null || content.isEmpty()) {
                return emptyFallback("No content in AI response");
            }

            // Parse the JSON content into AnalyzeLogsResponse
            AnalyzeLogsResponse response =
                    objectMapper.readValue(content, AnalyzeLogsResponse.class);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return emptyFallback("Exception while calling AI: " + e.getMessage());
        }
    }

    private String toJsonString(String text) {
        // Basic JSON escaping for the prompt
        return "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r") + "\"";
    }

    private String extractContentFromResponse(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode msg = choices.get(0).path("message");
                return msg.path("content").asText();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AnalyzeLogsResponse emptyFallback(String summaryMsg) {
        AnalyzeLogsResponse resp = new AnalyzeLogsResponse();
        resp.setSummary(summaryMsg);
        resp.setKeyErrors(java.util.List.of());
        resp.setPossibleCauses(java.util.List.of());
        resp.setSuggestedActions(java.util.List.of());
        return resp;
    }
}
