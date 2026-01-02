package com.beauver.Endpoints;

import com.beauver.Classes.Queue;
import com.beauver.Classes.QueueUser;
import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.StatusCodes;
import com.beauver.Security.JwtUtil;
import com.beauver.Security.VerifyJwt;
import com.beauver.Services.QueueService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/api/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueueAPI {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    QueueService queueService;

    @Inject
    QueueEventsAPI queueEventsAPI;

    @POST
    @VerifyJwt
    @Path("/join/{teacherId}")
    @Transactional
    @RunOnVirtualThread
    public String joinQueue(@HeaderParam("Authorization") String authorization, @PathParam("teacherId") Long teacherId) {
        String userId = jwtUtil.getIdFromHeader(authorization);
        User user = User.findById(userId);
        long classId = user.classEntity.id;

        if(queueService.isInQueue(userId, teacherId, classId)){
            return new Result<>(StatusCodes.FORBIDDEN, "You are already in this queue.").toJson();
        }

        Queue queue = queueService.findQueueByTeacherAndClass(teacherId, classId);
        if(queue == null || !queue.isEnabled){
            return new Result<>(StatusCodes.FORBIDDEN, "Queue is not available/open").toJson();
        }

        Result<?> result = queueService.joinQueue(userId, teacherId, classId);

        queueEventsAPI.broadcastQueueUpdate(queue.id, "user_joined_queue", Map.of(
                "userId", userId,
                "userName", user.name,
                "queueId", queue.id
        ));

        return result.toJson();
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

        Queue queue = queueService.findQueueByTeacherAndClass(teacherId, classId);

        queueEventsAPI.broadcastQueueUpdate(queue.id, "user_left_queue", Map.of(
                "userId", userId,
                "userName", user.name,
                "queueId", queue.id
        ));

        return queueService.leaveQueue(userId, teacherId, classId).toJson();
    }

    @POST
    @VerifyJwt
    @Path("/leaveByQueueId/{queueId}")
    @Transactional
    @RunOnVirtualThread
    public String leaveQueueByQueueId(@HeaderParam("Authorization") String authorization, @PathParam("queueId") Long queueId) {
        String userId = jwtUtil.getIdFromHeader(authorization);
        User user = User.findById(userId);

        queueEventsAPI.broadcastQueueUpdate(queueId, "user_left_queue", Map.of(
                "userId", userId,
                "userName", user.name,
                "queueId", queueId
        ));

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
