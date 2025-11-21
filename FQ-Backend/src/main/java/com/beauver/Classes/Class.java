package com.beauver.Classes;

import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "classes")
public class Class extends PanacheEntity {
    @Expose
    public long id;

    @Expose
    public String name;

    @Expose
    @OneToMany
    @JoinColumn(name = "class_id")
    public List<User> users;

    @Expose
    @ManyToMany
    @JoinTable(
            name = "class_teachers",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    public List<User> teachers;
}