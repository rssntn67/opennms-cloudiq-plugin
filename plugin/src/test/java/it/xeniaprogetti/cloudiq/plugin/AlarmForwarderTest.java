package it.xeniaprogetti.cloudiq.plugin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;

import org.junit.Test;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;

import it.xeniaprogetti.cloudiq.plugin.model.Alert;

public class AlarmForwarderTest {

    @Test
    public void canConvertAlarmToAlert() {
        Alert alert = new Alert();
        alert.setStatus(Alert.Status.CRITICAL);
        alert.setTimestamp(Instant.ofEpochSecond(1402302570));
        alert.setDescription("CPU is above upper limit (91%)");
        alert.setAttribute("my_unique_attribute", "my_unique_value");

        ImmutableInMemoryEvent event = CloudIqEventForwarder.toEvent(alert);

        assertThat(event.getSeverity(), equalTo(Severity.CRITICAL));
    }
}
