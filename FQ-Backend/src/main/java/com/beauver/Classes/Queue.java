package com.beauver.Classes;

import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "queues")
public class Queue extends PanacheEntity {
    @Expose
    public long id;

    @Expose
    public String name;

    @Expose
    public boolean isEnabled;

    @Expose
    @OneToOne
    public User teacher;

    @Expose
    @OneToMany(mappedBy = "queue")
    public List<QueueUser> queueUsers = new ArrayList<>();
}
