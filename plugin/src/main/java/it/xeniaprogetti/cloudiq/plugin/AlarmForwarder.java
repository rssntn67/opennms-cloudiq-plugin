package it.xeniaprogetti.cloudiq.plugin;

import java.util.Objects;

import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import it.xeniaprogetti.cloudiq.plugin.model.Alert;

public class AlarmForwarder {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmForwarder.class);

    private static final String UEI_PREFIX = "uei.opennms.org/cloudiq";
    private static final String SEND_EVENT_FAILED_UEI = UEI_PREFIX + "/sendEventFailed";
    private static final String SEND_EVENT_SUCCESSFUL_UEI = UEI_PREFIX + "/sendEventSuccessful";

    private final MetricRegistry metrics = new MetricRegistry();
    private final Meter eventsForwarded = metrics.meter("eventsForwarded");
    private final EventForwarder eventForwarder;

    public AlarmForwarder(EventForwarder eventForwarder) {
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
    }

    public void forward(Alert alert) {

        // Map the alarm to the corresponding model object that the API requires
        ImmutableInMemoryEvent event = toEvent(alert);

        eventsForwarded.mark();
        eventForwarder.sendAsync(event);
        LOG.info("Sending event for alert with reduction-key: {} .", alert);
    }



    public static ImmutableInMemoryEvent toEvent(Alert alert) {
        return ImmutableInMemoryEvent.newBuilder()
                .setUei(SEND_EVENT_FAILED_UEI)
                .addParameter(ImmutableEventParameter.newBuilder()
                        .setName("reductionKey")
                        .setValue(alert.getAttributes().get("reductionKey"))
                        .build())
                .addParameter(ImmutableEventParameter.newBuilder()
                        .setName("message")
                        .setValue(alert.getDescription())
                        .build())
                .build();
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
