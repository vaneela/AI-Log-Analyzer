package org.example.loganalyzer.controller;

import org.example.loganalyzer.dto.AnalyzeLogsRequest;
import org.example.loganalyzer.dto.AnalyzeLogsResponse;
import org.example.loganalyzer.service.LogAnalysisService;
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

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeLogsResponse> analyzeLogs(@RequestBody AnalyzeLogsRequest request) {
        AnalyzeLogsResponse response = logAnalysisService.analyze(request);
        return ResponseEntity.ok(response);
    }
}
