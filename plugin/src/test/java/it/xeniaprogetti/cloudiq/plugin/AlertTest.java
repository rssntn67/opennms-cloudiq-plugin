package it.xeniaprogetti.cloudiq.plugin;

import java.util.ArrayList;

import org.json.JSONException;
import org.junit.Test;
import it.xeniaprogetti.cloudiq.plugin.model.Alert;
import it.xeniaprogetti.cloudiq.plugin.model.ImpactedObject;
import it.xeniaprogetti.cloudiq.plugin.model.Issue;

import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class AlertTest {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies that the object is serialized to JSON as expected.
     */
    @Test
    public void canSerializeToJsonRaiseAlert() throws JsonProcessingException, JSONException {
        Alert alert = new Alert();
        alert.setSystemDisplayIdentifier("APM00000000000");
        alert.setSystemName("APM00000000000");
        alert.setSystemModel("Unity 400");
        alert.setTimestamp(1618325841541L);
        alert.setTimestampIso8601("2021-04-13T14:57Z");
        alert.setCurrentScore(70);
        Issue issue = getIssue();
        ImpactedObject impactedObject = new ImpactedObject();
        impactedObject.setObjectNativeId("Host_49");
        impactedObject.setObjectId("APM00000000000__HOST__Host_49");
        impactedObject.setObjectNativeType("HOST");
        impactedObject.setObjectName(null);
        issue.getImpactedObjects().add(impactedObject);
        alert.setNewIssues(new ArrayList<>());
        alert.setResolvedIssues(new ArrayList<>());
        alert.getNewIssues().add(issue);
        mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, false);

        String expectedJson = "{\n" +
                "  \"system_display_identifier\": \"APM00000000000\",\n" +
                "  \"system_name\": \"APM00000000000\",\n" +
                "  \"system_model\": \"Unity 400\",\n" +
                "  \"timestamp\": 1618325841541,\n" +
                "  \"timestamp_iso8601\": \"2021-04-13T14:57Z\",\n" +
                "  \"current_score\": 70,\n" +
                "  \"new_issues\": [\n" +
                "    {\n" +
                "      \"id\": \"6F17B328DA7D421207DB648C8A154B6130E32F7155B55F46D5A5312938D65B76\",\n" +
                "      \"impact\": -10,\n" +
                "      \"description\": \"Host 'test-host1' does not have connectivity to either SP.\",\n" +
                "      \"resolution\": \"This host is disconnected from both SPA and SPB. Review your connectivity to ensure that all hosts have a connection to both SPs. Health checks for this host can be paused under Admin -> Customization.\",\n" +
                "      \"rule_id\": \"UNITY_HOST_DISCONNECTED_RULE\",\n" +
                "      \"category\": \"CONFIGURATION\",\n" +
                "      \"impacted_objects\": [\n" +
                "        {\n" +
                "          \"object_native_id\": \"Host_49\",\n" +
                "          \"object_name\": null,\n" +
                "          \"object_id\": \"APM00000000000__HOST__Host_49\",\n" +
                "          \"object_native_type\": \"HOST\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"resolved_issues\": []\n" +
                "}";
        JSONAssert.assertEquals(expectedJson, mapper.writeValueAsString(alert), false);
    }

    private static Issue getIssue() {
        Issue issue = new Issue();
        issue.setId("6F17B328DA7D421207DB648C8A154B6130E32F7155B55F46D5A5312938D65B76");
        issue.setImpact(-10);
        issue.setDescription("Host 'test-host1' does not have connectivity to either SP.");
        issue.setResolution("This host is disconnected from both SPA and SPB. Review your connectivity to ensure that all hosts have a connection to both SPs. Health checks for this host can be paused under Admin -> Customization.");
        issue.setRuleId("UNITY_HOST_DISCONNECTED_RULE");
        issue.setCategory("CONFIGURATION");
        issue.setImpactedObjects(new ArrayList<>());
        return issue;
    }


    @Test
    public void canSerializeToJsonClear() throws JsonProcessingException, JSONException {
        Alert alert = new Alert();
        alert.setSystemDisplayIdentifier("APM00000000000");
        alert.setSystemName("APM00000000000");
        alert.setSystemModel("Unity 400");
        alert.setTimestamp(1618327944699L);
        alert.setTimestampIso8601("2021-04-13T15:32Z");
        alert.setCurrentScore(70);
        Issue issue = getIssue();
        ImpactedObject impactedObject = new ImpactedObject();
        impactedObject.setObjectNativeId("Host_49");
        impactedObject.setObjectId("APM00000000000__HOST__Host_49");
        impactedObject.setObjectNativeType("HOST");
        issue.getImpactedObjects().add(impactedObject);
        alert.setNewIssues(new ArrayList<>());
        alert.setResolvedIssues(new ArrayList<>());
        alert.getResolvedIssues().add(issue);


        String expectedJson = "{\n" +
                "  \"system_display_identifier\": \"APM00000000000\",\n" +
                "  \"system_name\": \"APM00000000000\",\n" +
                "  \"system_model\": \"Unity 400\",\n" +
                "  \"timestamp\": 1618327944699,\n" +
                "  \"timestamp_iso8601\": \"2021-04-13T15:32Z\",\n" +
                "  \"current_score\": 70,\n" +
                "  \"new_issues\": [],\n" +
                "  \"resolved_issues\": [\n" +
                "    {\n" +
                "      \"id\": \"6F17B328DA7D421207DB648C8A154B6130E32F7155B55F46D5A5312938D65B76\",\n" +
                "      \"impact\": -10,\n" +
                "      \"description\": \"Host 'test-host1' does not have connectivity to either SP.\",\n" +
                "      \"resolution\": \"This host is disconnected from both SPA and SPB. Review your connectivity to ensure that all hosts have a connection to both SPs. Health checks for this host can be paused under Admin -> Customization.\",\n" +
                "      \"rule_id\": \"UNITY_HOST_DISCONNECTED_RULE\",\n" +
                "      \"category\": \"CONFIGURATION\",\n" +
                "      \"impacted_objects\": [\n" +
                "        {\n" +
                "          \"object_native_id\": \"Host_49\",\n" +
                "          \"object_name\": null,\n" +
                "          \"object_id\": \"APM00000000000__HOST__Host_49\",\n" +
                "          \"object_native_type\": \"HOST\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedJson, mapper.writeValueAsString(alert), false);
    }

}
