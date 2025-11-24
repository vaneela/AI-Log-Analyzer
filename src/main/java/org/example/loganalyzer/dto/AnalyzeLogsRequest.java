package org.example.loganalyzer.dto;

public class AnalyzeLogsRequest {

    private String logs;
    private String environment; // e.g. "dev", "stage", "prod"

    public AnalyzeLogsRequest() {
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
