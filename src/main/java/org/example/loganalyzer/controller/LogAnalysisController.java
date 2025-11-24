package org.example.loganalyzer.controller;

import org.example.loganalyzer.dto.AnalyzeLogsRequest;
import org.example.loganalyzer.dto.AnalyzeLogsResponse;
import org.example.loganalyzer.service.LogAnalysisService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin // so your React frontend can call it
public class LogAnalysisController {

    private final LogAnalysisService logAnalysisService;

    public LogAnalysisController(LogAnalysisService logAnalysisService) {
        this.logAnalysisService = logAnalysisService;
    }

    // 1) Raw text logs
    @PostMapping(
            value = "/analyze",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AnalyzeLogsResponse> analyzeLogs(
            @RequestHeader(value = "X-Environment", defaultValue = "prod") String environment,
            @RequestBody String rawLogs
    ) {
        AnalyzeLogsRequest request = new AnalyzeLogsRequest();
        request.setEnvironment(environment);
        request.setLogs(rawLogs);

        AnalyzeLogsResponse response = logAnalysisService.analyze(request);
        return ResponseEntity.ok(response);
    }

    // 2) JSON payload
    @PostMapping(
            value = "/analyze/json",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AnalyzeLogsResponse> analyzeJson(@RequestBody AnalyzeLogsRequest request) {
        AnalyzeLogsResponse response = logAnalysisService.analyze(request);
        return ResponseEntity.ok(response);
    }

}
