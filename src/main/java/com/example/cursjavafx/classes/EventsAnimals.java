package com.example.cursjavafx.classes;

import java.util.Date;

public class EventsAnimals {
    int id;
    String name;
    Date date_start;
    Date date_end;

    public EventsAnimals(int id, String name, Date date_start, Date date_end) {
        this.id = id;
        this.name = name;
        this.date_start = date_start;
        this.date_end = date_end;
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

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }
}
