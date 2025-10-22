package com.beauver.Classes;

import com.beauver.Enums.Role;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Table(name = "users")
@Entity
public class User extends PanacheEntity {
    public String name;
    public String email;
    public String password;

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
