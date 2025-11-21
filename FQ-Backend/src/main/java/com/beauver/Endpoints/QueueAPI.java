package com.beauver.Endpoints;

import com.beauver.Classes.Result;
import com.beauver.Security.JwtUtil;
import com.beauver.Security.VerifyJwt;
import com.beauver.Services.QueueService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueueAPI {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    QueueService queueService;

    @POST
    @VerifyJwt
    @Path("/join/{teacherId}")
    @Transactional
    @RunOnVirtualThread
    public String joinQueue(@HeaderParam("Authorization") String authorization, @PathParam("teacherId") Long teacherId) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        return queueService.joinQueue(userId, teacherId).toJson();
    }

}
