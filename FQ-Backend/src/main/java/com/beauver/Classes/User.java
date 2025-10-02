package com.beauver.Classes;

import com.beauver.Enums.Role;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class User extends PanacheEntity {
    public String name;
    public String email;
    public String password;
    public Role role;

    public User() {

    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
