package com.beauver.Endpoints;

import com.beauver.Classes.User;
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
        User user = User.findById(userId);
        long classId = user.classEntity.id;

        return queueService.joinQueue(userId, teacherId, classId).toJson();
    }

    @POST
    @VerifyJwt
    @Path("/leaveByTeacherId/{teacherId}")
    @Transactional
    @RunOnVirtualThread
    public String leaveQueue(@HeaderParam("Authorization") String authorization, @PathParam("teacherId") Long teacherId) {
        String userId = jwtUtil.getIdFromHeader(authorization);
        User user = User.findById(userId);
        long classId = user.classEntity.id;

        return queueService.leaveQueue(userId, teacherId, classId).toJson();
    }

    @POST
    @VerifyJwt
    @Path("/leaveByQueueId/{queueId}")
    @Transactional
    @RunOnVirtualThread
    public String leaveQueueByQueueId(@HeaderParam("Authorization") String authorization, @PathParam("queueId") Long queueId) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        return queueService.leaveQueueByQueueId(userId, queueId).toJson();
    }

    @POST
    @VerifyJwt
    @Path("/getQueueByTeacherId/{teacherId}")
    @Transactional
    @RunOnVirtualThread
    public String getQueueByTeacherId(@HeaderParam("Authorization") String authorization, @PathParam("teacherId") Long teacherId) {
        String userId = jwtUtil.getIdFromHeader(authorization);
        User user = User.findById(userId);
        long classId = user.classEntity.id;

        return queueService.getQueueByTeacherId(teacherId, classId).toJson();
    }

    @GET
    @VerifyJwt
    @Path("/getQueueById/{queueId}")
    @Transactional
    @RunOnVirtualThread
    public String getQueueById(@PathParam("queueId") Long queueId) {
        return queueService.getQueueByQueueId(queueId).toJson();
    }

    @GET
    @VerifyJwt
    @Path("/getYourQueues")
    @Transactional
    @RunOnVirtualThread
    public String getYourQueues(@HeaderParam("Authorization") String authorization) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        return queueService.getYourQueues(userId).toJson();
    }

}
