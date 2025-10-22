package com.beauver.Classes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "queues")
public class Queue extends PanacheEntity {
    public String name;

    @OneToMany
    public List<User> users = new ArrayList<>();
}
