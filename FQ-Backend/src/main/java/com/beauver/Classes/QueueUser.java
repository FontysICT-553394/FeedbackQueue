package com.beauver.Classes;

import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "queue_users")
public class QueueUser extends PanacheEntity {

    @Expose
    public long id;

    @Expose
    @ManyToOne
    @JoinColumn(name = "queue_id")
    public Queue queue;

    @Expose
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
