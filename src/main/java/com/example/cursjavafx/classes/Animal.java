package com.example.cursjavafx.classes;

import javafx.collections.ObservableList;

import java.util.Date;

public class Animal {
    int id;
    String name;
    String kind;
    Date date_birth;

    public Animal(int id, String name, String kind, Date date_birth) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.date_birth = date_birth;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Date getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Date date_birth) {
        this.date_birth = date_birth;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", date_birth=" + date_birth +
                '}';
    }
}
