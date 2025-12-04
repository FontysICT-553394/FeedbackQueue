package com.beauver.Classes;

import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_users")
public class QueueUser extends PanacheEntityBase {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Expose
    @ManyToOne
    @JoinColumn(name = "queue_id", nullable = false)
    public Queue queue;

    @Expose
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Expose
    @CreationTimestamp
    public LocalDateTime joinedAt;
}
