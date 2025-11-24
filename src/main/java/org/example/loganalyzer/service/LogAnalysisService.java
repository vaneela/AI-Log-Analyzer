package org.example.loganalyzer.service;

import org.example.loganalyzer.dto.AnalyzeLogsRequest;
import org.example.loganalyzer.dto.AnalyzeLogsResponse;
import org.springframework.stereotype.Service;

@Service
public class LogAnalysisService {

    private final AiClient aiClient;

    public LogAnalysisService(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    public AnalyzeLogsResponse analyze(AnalyzeLogsRequest request) {
        String cleanedLogs = preProcessLogs(request.getLogs());
        String environment = request.getEnvironment() == null ? "unknown" : request.getEnvironment();

        String prompt = buildPrompt(cleanedLogs, environment);

        // Call AI and get structured response
        return aiClient.analyzeLogs(prompt);
    }

    private String preProcessLogs(String logs) {
        if (logs == null || logs.isEmpty()) {
            return "";
        }

        StringBuilder filtered = new StringBuilder();
        for (String line : logs.split("\\r?\\n")) {
            String upper = line.toUpperCase();
            if (upper.contains("ERROR") || upper.contains("WARN")) {
                filtered.append(line).append("\n");
            }
        }

        String result = filtered.toString().trim();
        return result.isEmpty() ? logs : result;
    }

    private String buildPrompt(String logs, String environment) {
        return """
                You are a senior backend engineer and expert in log analysis for distributed Java systems.

                Environment: %s

                Given the following application logs, you must:

                1. Provide a concise high-level summary of the main issues.
                2. List the distinct key error patterns (group similar errors together).
                3. Infer the most likely root causes.
                4. Suggest concrete next actions to debug or fix the issues.

                Respond STRICTLY in the following JSON format (valid JSON):

                {
                  "summary": "one short paragraph summary",
                  "keyErrors": ["error pattern 1", "error pattern 2"],
                  "possibleCauses": ["cause 1", "cause 2"],
                  "suggestedActions": ["action 1", "action 2"]
                }

                Do not include any extra text outside the JSON.

                Logs:
                ```text
                %s
                ```
                """.formatted(environment, logs);
    }
}
