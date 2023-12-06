
package it.xeniaprogetti.cloudiq.plugin.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({
    "id",
    "impact",
    "description",
    "resolution",
    "rule_id",
    "category",
    "impacted_objects"
})
public class Issue {

    @JsonProperty("id")
    private String id;
    @JsonProperty("impact")
    private Integer impact;
    @JsonProperty("description")
    private String description;
    @JsonProperty("resolution")
    private String resolution;
    @JsonProperty("rule_id")
    private String ruleId;
    @JsonProperty("category")
    private String category;
    @JsonProperty("impacted_objects")
    private List<ImpactedObject> impactedObjects;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("impact")
    public Integer getImpact() {
        return impact;
    }

    @JsonProperty("impact")
    public void setImpact(Integer impact) {
        this.impact = impact;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("resolution")
    public String getResolution() {
        return resolution;
    }

    @JsonProperty("resolution")
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("rule_id")
    public String getRuleId() {
        return ruleId;
    }

    @JsonProperty("rule_id")
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("impacted_objects")
    public List<ImpactedObject> getImpactedObjects() {
        return impactedObjects;
    }

    @JsonProperty("impacted_objects")
    public void setImpactedObjects(List<ImpactedObject> impactedObjects) {
        this.impactedObjects = impactedObjects;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", impact=" + impact +
                ", description='" + description + '\'' +
                ", resolution='" + resolution + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", category='" + category + '\'' +
                ", impactedObjects=" + impactedObjects +
                '}';
    }
}
