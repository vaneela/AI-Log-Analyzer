package org.example.loganalyzer.dto;

import java.util.List;

public class AnalyzeLogsResponse {

    private String summary;
    private List<String> keyErrors;
    private List<String> possibleCauses;
    private List<String> suggestedActions;

    public AnalyzeLogsResponse() {
    }

    public AnalyzeLogsResponse(String summary,
                               List<String> keyErrors,
                               List<String> possibleCauses,
                               List<String> suggestedActions) {
        this.summary = summary;
        this.keyErrors = keyErrors;
        this.possibleCauses = possibleCauses;
        this.suggestedActions = suggestedActions;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getKeyErrors() {
        return keyErrors;
    }

    public void setKeyErrors(List<String> keyErrors) {
        this.keyErrors = keyErrors;
    }

    public List<String> getPossibleCauses() {
        return possibleCauses;
    }

    public void setPossibleCauses(List<String> possibleCauses) {
        this.possibleCauses = possibleCauses;
    }

    public List<String> getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(List<String> suggestedActions) {
        this.suggestedActions = suggestedActions;
    }
}
