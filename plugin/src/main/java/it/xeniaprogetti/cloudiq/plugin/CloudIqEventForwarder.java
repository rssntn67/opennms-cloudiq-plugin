package it.xeniaprogetti.cloudiq.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import it.xeniaprogetti.cloudiq.plugin.model.Alert;
import it.xeniaprogetti.cloudiq.plugin.model.ImpactedObject;
import it.xeniaprogetti.cloudiq.plugin.model.Issue;

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
            LOG.info("forward: no node id found for alert {}", alert);
        }
        // Map the alarm to the corresponding model object that the API requires
        toEvent(alert,node).forEach( event -> {
            if (event.getSeverity() == Severity.NORMAL)
                cleanEventsForwarded.mark();
            else
                raiseEventsForwarded.mark();
            LOG.info("Sending event: {} .", event);
            eventForwarder.sendAsync(event);

        });

    }

    public static ImmutableInMemoryEvent.Builder eventBuilder(Alert alert, Node node) {
        ImmutableInMemoryEvent.Builder eventBuilder= ImmutableInMemoryEvent.newBuilder();
        eventBuilder.setService("CloudIQ");
        eventBuilder.setSource("CloudIQPlugin");
        if (node != null)
            eventBuilder.setNodeId(node.getId());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("SystemName").setValue(alert.getSystemName()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("SystemModel").setValue(alert.getSystemModel()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("TimestampIso8601").setValue(alert.getTimestampIso8601()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("current_score").setValue(alert.getCurrentScore().toString()).build());
        return eventBuilder;
    }

    public static void build(ImmutableInMemoryEvent.Builder eventBuilder, Issue issue) {
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("id").setValue(issue.getId()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("impact").setValue(issue.getImpact().toString()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("description").setValue(issue.getDescription()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("resolution").setValue(issue.getResolution()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("rule_id").setValue(issue.getRuleId()).build());
        eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                .setName("category").setValue(issue.getCategory()).build());

        int i = 0;
        for (ImpactedObject iobj : issue.getImpactedObjects()) {
            eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("object_native_id_"+i).setValue(iobj.getObjectNativeId()).build());
            eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("object_name_"+i).setValue(iobj.getObjectName()).build());
            eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("object_id_"+i).setValue(iobj.getObjectId()).build());
            eventBuilder.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("object_native_type_"+i).setValue(iobj.getObjectNativeType()).build());
        }
    }
    public static List<ImmutableInMemoryEvent> toEvent(Alert alert,Node node) {

        List<ImmutableInMemoryEvent> eventList = new ArrayList<>();

        for (Issue issue : alert.getResolvedIssues()) {
            ImmutableInMemoryEvent.Builder eventBuilder = eventBuilder(alert, node);
            eventBuilder.setUei(CLEAN_EVENT_UEI);
            eventBuilder.setSeverity(Severity.NORMAL);
            build(eventBuilder, issue);
            eventList.add(eventBuilder.build());
        }
        for (Issue issue : alert.getNewIssues()) {
            ImmutableInMemoryEvent.Builder eventBuilder = eventBuilder(alert,node);
            eventBuilder.setUei(RAISE_EVENT_UEI);
            eventBuilder.setSeverity(Severity.CRITICAL);
            build(eventBuilder,issue);
            eventList.add(eventBuilder.build());
        }
        return eventList;
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
