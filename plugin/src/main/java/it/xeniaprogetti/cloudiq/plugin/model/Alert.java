
package it.xeniaprogetti.cloudiq.plugin.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "system_display_identifier",
    "system_name",
    "system_model",
    "timestamp",
    "timestamp_iso8601",
    "current_score",
    "new_issues",
    "resolved_issues"
})
public class Alert {

    @JsonProperty("system_display_identifier")
    private String systemDisplayIdentifier;
    @JsonProperty("system_name")
    private String systemName;
    @JsonProperty("system_model")
    private String systemModel;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("timestamp_iso8601")
    private String timestampIso8601;
    @JsonProperty("current_score")
    private Integer currentScore;
    @JsonProperty("new_issues")
    private List<Issue> newIssues;
    @JsonProperty("resolved_issues")
    private List<Issue> resolvedIssues;

    @JsonProperty("system_display_identifier")
    public String getSystemDisplayIdentifier() {
        return systemDisplayIdentifier;
    }

    @JsonProperty("system_display_identifier")
    public void setSystemDisplayIdentifier(String systemDisplayIdentifier) {
        this.systemDisplayIdentifier = systemDisplayIdentifier;
    }

    @JsonProperty("system_name")
    public String getSystemName() {
        return systemName;
    }

    @JsonProperty("system_name")
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @JsonProperty("system_model")
    public String getSystemModel() {
        return systemModel;
    }

    @JsonProperty("system_model")
    public void setSystemModel(String systemModel) {
        this.systemModel = systemModel;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("timestamp_iso8601")
    public String getTimestampIso8601() {
        return timestampIso8601;
    }

    @JsonProperty("timestamp_iso8601")
    public void setTimestampIso8601(String timestampIso8601) {
        this.timestampIso8601 = timestampIso8601;
    }

    @JsonProperty("current_score")
    public Integer getCurrentScore() {
        return currentScore;
    }

    @JsonProperty("current_score")
    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }

    @JsonProperty("new_issues")
    public List<Issue> getNewIssues() {
        return newIssues;
    }

    @JsonProperty("new_issues")
    public void setNewIssues(List<Issue> newIssues) {
        this.newIssues = newIssues;
    }

    @JsonProperty("resolved_issues")
    public List<Issue> getResolvedIssues() {
        return resolvedIssues;
    }

    @JsonProperty("resolved_issues")
    public void setResolvedIssues(List<Issue> resolvedIssues) {
        this.resolvedIssues = resolvedIssues;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "systemDisplayIdentifier='" + systemDisplayIdentifier + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemModel='" + systemModel + '\'' +
                ", timestamp=" + timestamp +
                ", timestampIso8601='" + timestampIso8601 + '\'' +
                ", currentScore=" + currentScore +
                ", newIssues=" + newIssues +
                ", resolvedIssues=" + resolvedIssues +
                '}';
    }
}
