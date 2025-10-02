package com.beauver.Classes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.List;

public class Class extends PanacheEntity {

    public String name;
    public List<User> users;


}
