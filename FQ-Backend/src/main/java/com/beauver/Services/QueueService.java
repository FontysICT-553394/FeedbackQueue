package com.beauver.Services;

import com.beauver.Classes.Queue;
import com.beauver.Classes.QueueUser;
import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.StatusCodes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class QueueService {

    @PersistenceContext
    private EntityManager entityManager;

    public Result<?> joinQueue(String userId, long teacherId){
        if(isInQueue(userId, teacherId)){
            return new Result<>(StatusCodes.FORBIDDEN, "You are already in this queue.");
        }

        System.out.println("Not in queue");

        Queue queue = findQueueByTeacherId(teacherId);
        if(queue == null || !queue.isEnabled){
            return new Result<>(StatusCodes.FORBIDDEN, "Queue is not available/open");
        }

        System.out.println("found queue");


        QueueUser queueUser = new QueueUser();
        queueUser.user = User.findById(userId);
        queueUser.queue = queue;
        entityManager.persist(queueUser);

        System.out.println("Joined Queue");


        return new Result<>(StatusCodes.OK, "Joined queue successfully");
    }

    public Result<?> leaveQueue(String userId, long teacherId){
        QueueUser queueUser = findQueueUser(userId, teacherId);
        if(queueUser == null){
            return new Result<>(StatusCodes.NOT_FOUND, "You are not in this queue");
        }

        entityManager.remove(queueUser);
        return new Result<>(StatusCodes.OK, "Left queue successfully");
    }

    private boolean isInQueue(String userId, long teacherId){
        return entityManager.createQuery(
                        "SELECT COUNT(qu) > 0 FROM QueueUser qu " +
                                "WHERE qu.queue.teacher.id = :teacherId AND qu.user.id = :userId",
                        Boolean.class)
                .setParameter("teacherId", teacherId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    private QueueUser findQueueUser(String userId, long teacherId){
        return entityManager.createQuery(
                        "SELECT qu FROM QueueUser qu " +
                                "WHERE qu.queue.teacher.id = :teacherId AND qu.user.id = :userId",
                        QueueUser.class)
                .setParameter("teacherId", teacherId)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private Queue findQueueByTeacherId(long teacherId){
        return entityManager.createQuery(
                        "SELECT q FROM Queue q WHERE q.teacher.id = :teacherId",
                        Queue.class)
                .setParameter("teacherId", teacherId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private void createQueue(long teacherId){
        // Implementation needed
    }

}