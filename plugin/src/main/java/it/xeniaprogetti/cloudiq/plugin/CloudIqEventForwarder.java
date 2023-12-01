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
            return;
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

    public static List<ImmutableInMemoryEvent> toEvent(Alert alert,Node nd) {

        ImmutableInMemoryEvent.Builder IME= ImmutableInMemoryEvent.newBuilder();
        List<ImmutableInMemoryEvent> eventList = new ArrayList<>();
        
        if (alert.getNewIssues() == null || alert.getNewIssues().isEmpty()) {            

            IME.setUei(CLEAN_EVENT_UEI);
            IME.setSeverity(Severity.NORMAL);
            IME.setService("CloudIQ");
            IME.setSource("CloudIQPlugin");
            IME.setNodeId(nd.getId());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("SystemName").setValue(alert.getSystemName()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("SystemModel").setValue(alert.getSystemModel()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("TimestampIso8601").setValue(alert.getTimestampIso8601()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("current_score").setValue(alert.getCurrentScore().toString()).build());
            //ResolvedIssue
            for (Issue ise : alert.getResolvedIssues()) {
 
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("id").setValue(ise.getId()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("impact").setValue(ise.getImpact().toString()).build());                
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("description").setValue(ise.getDescription()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("resolution").setValue(ise.getResolution()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("rule_id").setValue(ise.getRuleId()).build());                
                IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("category").setValue(ise.getCategory()).build());                
                //ImpactedObject
                for (ImpactedObject iobj : ise.getImpactedObjects()) {
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("object_native_id").setValue(iobj.getObjectNativeId()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("object_name").setValue(iobj.getObjectName()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("object_id").setValue(iobj.getObjectId()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                          .setName("object_native_type").setValue(iobj.getObjectNativeType()).build());                    
                }

                eventList.add(IME.build());
            } //END FOR
        } else {//END IF
            IME.setUei(RAISE_EVENT_UEI);
            IME.setSeverity(Severity.CRITICAL);
            IME.setService("CloudIQ");
            IME.setSource("CloudIQPlugin");
            IME.setNodeId(nd.getId());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("SystemName").setValue(alert.getSystemName()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("SystemModel").setValue(alert.getSystemModel()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("TimestampIso8601").setValue(alert.getTimestampIso8601()).build());
            IME.addParameter(ImmutableEventParameter.newBuilder()
                    .setName("current_score").setValue(alert.getCurrentScore().toString()).build());
            //Issue
            for (Issue ise : alert.getNewIssues()) {

                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("id").setValue(ise.getId()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("impact").setValue(ise.getImpact().toString()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("description").setValue(ise.getDescription()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("resolution").setValue(ise.getResolution()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("rule_id").setValue(ise.getRuleId()).build());
                IME.addParameter(ImmutableEventParameter.newBuilder()
                        .setName("category").setValue(ise.getCategory()).build());
                //ImpactedObject
                for (ImpactedObject iobj : ise.getImpactedObjects()) {
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("object_native_id").setValue(iobj.getObjectNativeId()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("object_name").setValue(iobj.getObjectName()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("object_id").setValue(iobj.getObjectId()).build());
                    IME.addParameter(ImmutableEventParameter.newBuilder()
                            .setName("object_native_type").setValue(iobj.getObjectNativeType()).build());
                }
                eventList.add(IME.build());
            }
        }
        return eventList;
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
