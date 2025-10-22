package com.beauver.Classes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "classes")
public class Class extends PanacheEntity {

    public String name;

    @OneToMany
    public List<User> users;
}
