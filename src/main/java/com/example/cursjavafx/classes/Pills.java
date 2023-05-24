package com.example.cursjavafx.classes;

import java.util.Date;

public class Pills {
    int id;
    String name;
    int days;

    public Pills(int id, String name, int days) {
        this.id = id;
        this.name = name;
        this.days = days;
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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Pills{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", days=" + days +
                '}';
    }
}
