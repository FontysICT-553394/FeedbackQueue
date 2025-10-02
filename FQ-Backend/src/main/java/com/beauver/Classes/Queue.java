package com.beauver.Classes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.Dictionary;

public class Queue extends PanacheEntity {

    public String name;
    public Dictionary<Integer, User> queue;

}
