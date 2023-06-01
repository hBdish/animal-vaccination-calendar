package com.example.cursjavafx.database;

import com.example.cursjavafx.HelloApplication;
import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.classes.Pills;
import com.example.cursjavafx.utils.Scenes;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface dbDao {
    public static final dbDao singleDb = null;

    static final String DB_URL = null;
    static final String USER = null;
    static final String PASS = null;

    Connection connection = null;

    void setConnection();

    public void createUser(String name, String login, String password);

    public void loginUser(String login, String password, ActionEvent event);

    public ObservableList<Animal> getAnimals();

    public ObservableList<String> getKinds();

    public void createAnimal(String name, String kind, LocalDate date, ActionEvent event, Boolean reglament);

    public void deleteAnimal(int id, ActionEvent event);

    public ObservableList<EventsAnimals> getEvents();

    public void createEvent(String name, LocalDate date_start, LocalDate date_end, ActionEvent event);

    public void createEventreglament(String name, LocalDate date_start, LocalDate date_end);

    public void deleteEvent(int id, ActionEvent event);

    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus);

    Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities);

    public ObservableList<Pills> getPills();

    public ObservableList<String> getPrescribing();

    private LocalDate calcDateEnd(LocalDate dateStart, int days) {
        java.util.Date date = new java.util.Date(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        date.setDate(date.getDate() + days);
        LocalDate dateEnd = LocalDate.of(date.getYear(), date.getMonth() + 1, date.getDay() + 16);

        return dateEnd;

    }
}
