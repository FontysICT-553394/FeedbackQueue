package com.beauver.Endpoints;

import com.google.gson.Gson;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/api/queue/events")
@ApplicationScoped
public class QueueEventsAPI {

    private final Map<Long, BroadcastProcessor<String>> queueProcessors = new ConcurrentHashMap<>();

    @GET
    @Path("/{queueId}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public Multi<String> streamQueueEvents(@PathParam("queueId") Long queueId) {
        BroadcastProcessor<String> processor = queueProcessors.computeIfAbsent(
                queueId,
                id -> BroadcastProcessor.create()
        );
        return processor;
    }

    public void broadcastQueueUpdate(Long queueId, String eventType, Map<String, Object> data) {
        BroadcastProcessor<String> processor = queueProcessors.get(queueId);
        if (processor != null) {
            String eventData = String.format("{\"type\":\"%s\",\"data\":%s}",
                    eventType,
                    new Gson().toJson(data)
            );
            processor.onNext(eventData);
        }
    }
}
