package com.beauver.Services;

import com.beauver.Classes.Queue;
import com.beauver.Classes.QueueUser;
import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.StatusCodes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Map;

@ApplicationScoped
public class QueueService {

    @PersistenceContext
    private EntityManager entityManager;

    public Result<?> joinQueue(String userId, long teacherId, long classId){
        if(isInQueue(userId, teacherId, classId)){
            return new Result<>(StatusCodes.FORBIDDEN, "You are already in this queue.");
        }

        Queue queue = findQueueByTeacherAndClass(teacherId, classId);
        if(queue == null || !queue.isEnabled){
            return new Result<>(StatusCodes.FORBIDDEN, "Queue is not available/open");
        }

        QueueUser queueUser = new QueueUser();
        queueUser.user = User.findById(userId);
        queueUser.queue = queue;
        entityManager.persist(queueUser);

        return new Result<>(StatusCodes.OK, "Joined queue successfully");
    }

    public Result<?> leaveQueue(String userId, long teacherId, long classId){
        QueueUser queueUser = findQueueUser(userId, teacherId, classId);
        if(queueUser == null){
            return new Result<>(StatusCodes.NOT_FOUND, "You are not in this queue");
        }

        entityManager.remove(queueUser);
        return new Result<>(StatusCodes.OK, "Left queue successfully");
    }

    public Result<?> leaveQueueByQueueId(String userId, long queueId){
        QueueUser queueUser = entityManager.createQuery(
                        "SELECT qu FROM QueueUser qu " +
                                "WHERE qu.queue.id = :queueId " +
                                "AND qu.user.id = :userId",
                        QueueUser.class)
                .setParameter("queueId", queueId)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if(queueUser == null){
            return new Result<>(StatusCodes.NOT_FOUND, "You are not in this queue, UID: " + userId + " QID: " + queueId);
        }

        entityManager.remove(queueUser);
        return new Result<>(StatusCodes.OK, "Left queue successfully");
    }

    public Result<?> getQueueByTeacherId(long teacherId, long classId){
        Queue queue = findQueueByTeacherAndClass(teacherId, classId);
        if(queue == null) {
            return new Result<>(StatusCodes.NOT_FOUND, "Queue not found");
        }

        var queueUsers = entityManager.createQuery(
                        "SELECT qu FROM QueueUser qu " +
                                "WHERE qu.queue.teacher.id = :teacherId " +
                                "AND qu.queue.classEntity.id = :classId " +
                                "ORDER BY qu.joinedAt ASC",
                        QueueUser.class)
                .setParameter("teacherId", teacherId)
                .setParameter("classId", classId)
                .getResultList();

        var response = new java.util.HashMap<String, Object>();
        response.put("queueId", queue.id);
        response.put("queueName", queue.name);
        response.put("teacher", Map.of(
                "id", queue.teacher.id,
                "name", queue.teacher.name,
                "role", queue.teacher.role
        ));
        response.put("users", queueUsers.stream().map(qu -> Map.of(
                "id", qu.id,
                "userId", qu.user.id,
                "userName", qu.user.name,
                "joinedAt", qu.joinedAt
        )).toList());

        return new Result<>(StatusCodes.OK, response);
    }

    public Result<?> getQueueByQueueId(long queueId){
        Queue queue = entityManager.find(Queue.class, queueId);
        if(queue == null){
            return new Result<>(StatusCodes.NOT_FOUND, "Queue not found");
        }

        var queueUsers = entityManager.createQuery(
                        "SELECT qu FROM QueueUser qu " +
                                "WHERE qu.queue.id = :queueId " +
                                "ORDER BY qu.joinedAt ASC",
                        QueueUser.class)
                .setParameter("queueId", queueId)
                .getResultList();

        var response = new java.util.HashMap<String, Object>();
        response.put("queueId", queue.id);
        response.put("queueName", queue.name);
        response.put("teacher", Map.of(
                "id", queue.teacher.id,
                "name", queue.teacher.name,
                "role", queue.teacher.role
        ));
        response.put("users", queueUsers.stream().map(qu -> Map.of(
                "id", qu.id,
                "userId", qu.user.id,
                "userName", qu.user.name,
                "joinedAt", qu.joinedAt
        )).toList());

        return new Result<>(StatusCodes.OK, response);
    }

    public Result<?> getYourQueues(String userId){
        var queues = entityManager.createQuery(
                        "SELECT DISTINCT q FROM QueueUser qu " +
                                "JOIN qu.queue q " +
                                "WHERE qu.user.id = :userId",
                        Queue.class)
                .setParameter("userId", userId)
                .getResultList();

        return new Result<>(StatusCodes.OK, queues);
    }

    private boolean isInQueue(String userId, long teacherId, long classId){
        return entityManager.createQuery(
                        "SELECT COUNT(qu) > 0 FROM QueueUser qu " +
                                "WHERE qu.queue.teacher.id = :teacherId " +
                                "AND qu.queue.classEntity.id = :classId " +
                                "AND qu.user.id = :userId",
                        Boolean.class)
                .setParameter("teacherId", teacherId)
                .setParameter("classId", classId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    private QueueUser findQueueUser(String userId, long teacherId, long classId){
        return entityManager.createQuery(
                        "SELECT qu FROM QueueUser qu " +
                                "WHERE qu.queue.teacher.id = :teacherId " +
                                "AND qu.queue.classEntity.id = :classId " +
                                "AND qu.user.id = :userId",
                        QueueUser.class)
                .setParameter("teacherId", teacherId)
                .setParameter("classId", classId)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private Queue findQueueByTeacherAndClass(long teacherId, long classId){
        return entityManager.createQuery(
                        "SELECT q FROM Queue q " +
                                "WHERE q.teacher.id = :teacherId " +
                                "AND q.classEntity.id = :classId",
                        Queue.class)
                .setParameter("teacherId", teacherId)
                .setParameter("classId", classId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
