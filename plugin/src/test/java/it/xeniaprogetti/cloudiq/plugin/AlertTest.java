package it.xeniaprogetti.cloudiq.plugin;

import java.time.Instant;

import org.json.JSONException;
import org.junit.Test;
import it.xeniaprogetti.cloudiq.plugin.model.Alert;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlertTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies that the object is serialized to JSON as expected.
     */
    @Test
    public void canSerializeToJsonRaise() throws JsonProcessingException, JSONException {
        Alert alert = new Alert();
        alert.setSystemDisplayIdentifier("APM00000000000");
        alert.setSystemName("APM00000000000");
        alert.setSystemModel("Unity 400");
        alert.setTimestamp(1618327944699L);
        alert.setTimestampIso8601("2021-04-13T15:32Z");
        alert.setCurrentScore(70);


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


    @Test
    public void canSerializeToJsonClear() throws JsonProcessingException, JSONException {
        Alert alert = new Alert();
        alert.setSystemDisplayIdentifier("APM00000000000");
        alert.setSystemName("APM00000000000");
        alert.setSystemModel("Unity 400");
        alert.setTimestamp(1618327944699L);
        alert.setTimestampIso8601("2021-04-13T15:32Z");
        alert.setCurrentScore(70);


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
