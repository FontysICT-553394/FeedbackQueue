package com.beauver.Classes;

import com.beauver.Enums.Role;
import com.google.gson.annotations.Expose;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Table(name = "users")
@Entity
public class User extends PanacheEntityBase {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Expose
    public String name;

    public String email;
    public String password;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @Expose(serialize = false, deserialize = false)
    public Class classEntity;

    @Expose
    @Enumerated(EnumType.STRING)
    public Role role;

    public User(){}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}
