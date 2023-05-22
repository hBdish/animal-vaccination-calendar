package com.example.cursjavafx.classes;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String event_name;
    private String clientName;
    private Boolean eventInfo;


    public CalendarActivity(ZonedDateTime date, String event_name, String clientName, Boolean eventInfo) {
        this.date = date;
        this.event_name = event_name;
        this.clientName = clientName;
        this.eventInfo = eventInfo;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public Boolean getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(Boolean eventInfo) {
        this.eventInfo = eventInfo;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", event_name='" + event_name + '\'' +
                ", clientName='" + clientName + '\'' +
                ", eventInfo='" + eventInfo + '\'' +
                '}';
    }
}
