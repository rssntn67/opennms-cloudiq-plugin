
package it.xeniaprogetti.cloudiq.plugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "object_native_id",
    "object_name",
    "object_id",
    "object_native_type"
})
public class ImpactedObject {

    @JsonProperty("object_native_id")
    private String objectNativeId;
    @JsonProperty("object_name")
    private String objectName;
    @JsonProperty("object_id")
    private String objectId;
    @JsonProperty("object_native_type")
    private String objectNativeType;

    @JsonProperty("object_native_id")
    public String getObjectNativeId() {
        return objectNativeId;
    }

    @JsonProperty("object_native_id")
    public void setObjectNativeId(String objectNativeId) {
        this.objectNativeId = objectNativeId;
    }

    @JsonProperty("object_name")
    public String getObjectName() {
        return objectName;
    }

    @JsonProperty("object_name")
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @JsonProperty("object_id")
    public String getObjectId() {
        return objectId;
    }

    @JsonProperty("object_id")
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @JsonProperty("object_native_type")
    public String getObjectNativeType() {
        return objectNativeType;
    }

    @JsonProperty("object_native_type")
    public void setObjectNativeType(String objectNativeType) {
        this.objectNativeType = objectNativeType;
    }

}
