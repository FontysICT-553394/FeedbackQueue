package com.beauver.Classes;

import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "queues")
public class Queue extends PanacheEntityBase {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Expose
    public String name;

    @Expose
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    public User teacher;

    @Expose
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    public Class classEntity;

    @Expose
    @Column(nullable = false)
    public boolean isEnabled = true;
}
