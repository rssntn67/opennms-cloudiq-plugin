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
        alert.setTimestamp(1402302570L);
        alert.setSystemName("CPU is above upper limit (91%)");

        //ImmutableInMemoryEvent event = CloudIqEventForwarder.toEvent(alert);

        //assertThat(event.getSeverity(), equalTo(Severity.CRITICAL));
    }
}
