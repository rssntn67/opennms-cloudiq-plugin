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
            node = getNodeByAlert(alert);
        }
        // Map the alarm to the corresponding model object that the API requires
        ImmutableInMemoryEvent event = toEvent(alert,node);

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


    public static ImmutableInMemoryEvent toEvent(Alert alert,Node nd) {
        ImmutableInMemoryEvent.Builder IME= ImmutableInMemoryEvent.newBuilder();
        
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
            }

            /*
            return ImmutableInMemoryEvent.newBuilder()
                    .setUei(CLEAN_EVENT_UEI)
                    .setSeverity(Severity.NORMAL)
                    .setService("CloudIQ")
                    .setSource("CloudIQPlugin")
                    .setNodeId(nd.getId())
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("SystemName")
                            .setValue(alert.getSystemName())
                            .build())
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("SystemModel")
                            .setValue(alert.getSystemModel())
                            .build())
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("TimestampIso8601")
                            .setValue(alert.getTimestampIso8601().toString())
                            .build())                   
                    .addParameter(ImmutableEventParameter.newBuilder()
                            .setName("current_score")
                            .setValue(alert.getCurrentScore().toString())
                            .build())                                       
                    .build();
                    */
            return IME.build();
        }
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
            }
        /*
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
        */
        return IME.build();
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
