package it.xeniaprogetti.cloudiq.plugin;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeAssetRecord;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.SnmpInterface;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import it.xeniaprogetti.cloudiq.plugin.model.Alert;

public class CloudIqEventForwarder {
    private static final Logger LOG = LoggerFactory.getLogger(CloudIqEventForwarder.class);

    private static final String UEI_PREFIX = "uei.opennms.org/cloudiq";
    private static final String RAISE_EVENT_UEI = UEI_PREFIX + "/raiseEvent";
    private static final String CLEAN_EVENT_UEI = UEI_PREFIX + "/cleanEvent";

    private final MetricRegistry metrics = new MetricRegistry();
    private final Meter raiseEventsForwarded = metrics.meter("raiseEventsForwarded");
    private final Meter cleanEventsForwarded = metrics.meter("cleanEventsForwarded");
    private final EventForwarder eventForwarder;

    private final NodeDao nodeDao;

    public CloudIqEventForwarder(EventForwarder eventForwarder, NodeDao nodeDao) {
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    public void forward(Alert alert) {

        Node node = nodeDao.getNodeByLabel(alert.getSystemName());
        if (node == null) {
            node = getNodeByAlert(alert);
        }
        // Map the alarm to the corresponding model object that the API requires
        ImmutableInMemoryEvent event = toEvent(alert);

        if (event.getSeverity() == Severity.NORMAL)
            cleanEventsForwarded.mark();
        else
            raiseEventsForwarded.mark();
        LOG.info("Sending event: {} .", event);
        eventForwarder.sendAsync(event);
    }

    private Node getNodeByAlert(Alert alert) {
        throw new UnsupportedOperationException();
    }


    public static ImmutableInMemoryEvent toEvent(Alert alert) {
        if (alert.getNewIssues() == null || alert.getNewIssues().isEmpty()) {

            return ImmutableInMemoryEvent.newBuilder()
                    .setUei(CLEAN_EVENT_UEI)
                    .setSeverity(Severity.NORMAL)
                    .setService("CloudIQ")
                    .setSource("CloudIQPlugin")
                    .setNodeId(1)
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("reductionKey")
                            .setValue("NAZZ")
                            .build())
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("message")
                            .build())
                    .build();
        }
        return ImmutableInMemoryEvent.newBuilder()
                .setUei(RAISE_EVENT_UEI)
                .setSeverity(Severity.CRITICAL)
                .setNodeId(1)
                .setService("CloudIQ")
                .setSource("CloudIQPlugin")
                .addParameter(ImmutableEventParameter.newBuilder()
                        .setName("reductionKey")
                        .setValue("azz")
                        .build())
                .addParameter(ImmutableEventParameter.newBuilder()
                        .setName("message")
                        .build())
                .build();
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
