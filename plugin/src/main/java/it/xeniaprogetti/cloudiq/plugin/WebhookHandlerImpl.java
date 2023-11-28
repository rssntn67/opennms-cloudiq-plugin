package it.xeniaprogetti.cloudiq.plugin;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.xeniaprogetti.cloudiq.plugin.model.Alert;

public class WebhookHandlerImpl implements WebhookHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WebhookHandlerImpl.class);

    private final CloudIqEventForwarder forwarder;

    public WebhookHandlerImpl(CloudIqEventForwarder forwarder) {
        this.forwarder = forwarder;
    }


    @Override
    public Response ping() {
        return Response.ok("pong").build();
    }

    @Override
    public Response handleWebhook(Alert body) {
        LOG.debug("Got Alert: {}", body);
        forwarder.forward(body);
        return Response.ok().build();
    }
}

