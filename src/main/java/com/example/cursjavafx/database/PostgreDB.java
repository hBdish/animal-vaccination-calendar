package com.example.cursjavafx.database;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.HelloApplication;
import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.classes.Pills;
import com.example.cursjavafx.utils.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreDB {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/java";
    static final String USER = "postgres";
    static final String PASS = "1";
    private Connection connection = null;

    private void setConnection() {
        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }

    public void createUser(String name, String login, String password) {
        setConnection();
        String query = "INSERT INTO users(name, login, password) VALUES(?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, login);
            pst.setString(3, password);
            pst.executeUpdate();
            System.out.println("success created user");

        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loginUser(String login, String password, ActionEvent event) {
        setConnection();


        String query = "SELECT * FROM users WHERE login = ? AND password = ?";
        ResultSet resultSet;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.setString(2, password);
            resultSet = pst.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    HelloApplication.idUser = resultSet.getInt("id");
                    String log = resultSet.getString("login");
                    String pass = resultSet.getString("password");

                    if (log.equals(login) && pass.equals(password)) {
                        Scenes.MAIN.switchScene(event);
                    } else {
                        System.out.println("Login or password did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ObservableList<Animal> getAnimals() {
        setConnection();

        ObservableList<Animal> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM animals JOIN kinds ON animals.kind_id = kinds.id WHERE user_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, HelloApplication.idUser);
            resultSet = pst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("animal res empty");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("animal res empty");
                alert.show();
            } else {
                System.out.println("animal not empty");
            }

            try {
                while (resultSet.next()) {
                    Animal a =
                            new Animal(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("kind"),
                                    resultSet.getDate("date_birth")
                            );
                    data.add(a);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);

        }
        return data;
    }

    public ObservableList<String> getKinds() {
        setConnection();

        ObservableList<String> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM kinds";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    data.add(resultSet.getString("kind"));
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
        return data;
    }

    public void createAnimal(String name, String kind, LocalDate date, ActionEvent event) {
        setConnection();

        int kind_id = -1;
        ResultSet resultSet;

        String query = "SELECT id FROM kinds WHERE kind = ?";
        String query2 = "INSERT INTO animals(name, kind_id, date_birth, user_id) VALUES(?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, kind);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    kind_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(query2)) {
            pst.setString(1, name);
            pst.setInt(2, kind_id);
            pst.setDate(3, Date.valueOf(date));
            pst.setInt(4, HelloApplication.idUser);
            pst.executeUpdate();
            System.out.println("success created animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {

            try {
                connection.close();
                Scenes.MAIN.switchScene(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void deleteAnimal(int id, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query = "DELETE FROM animals WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("delete animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
                Scenes.MAIN.switchScene(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ObservableList<EventsAnimals> getEvents() {
        setConnection();

        ObservableList<EventsAnimals> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM events WHERE animal_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, HelloApplication.idAnimal);
            resultSet = pst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("event res empty");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("event res empty");
                alert.show();
            } else {
                System.out.println("animal not empty");
            }

            try {
                while (resultSet.next()) {
                    EventsAnimals event =
                            new EventsAnimals(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getDate("date_start"),
                                    resultSet.getDate("date_end")
                            );
                    data.add(event);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);

        }
        return data;
    }

    public void createEvent(String name, LocalDate date_start, LocalDate date_end, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query1 = "INSERT INTO events(name, date_start, date_end, animal_id) VALUES(?, ?, ?, ?)";


        try (PreparedStatement pst = connection.prepareStatement(query1)) {
            pst.setString(1, name);
            pst.setDate(2, Date.valueOf(date_start));
            pst.setDate(3, Date.valueOf(date_end));
            pst.setInt(4, HelloApplication.idAnimal);
            pst.executeUpdate();
            System.out.println("success created event");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {

            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Scenes.ANIMAL_EVENTS.switchScene(event);
        }
    }

    public void deleteEvent(int id, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query = "DELETE FROM events WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("delete animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
                Scenes.ANIMAL_EVENTS.switchScene(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        setConnection();

        List<CalendarActivity> calendarActivities = new ArrayList<>();
        ResultSet resultSet;

        String query = "SELECT animals.name as animal_name, events.name as event_name, events.date_start as event_date_start, events.date_end as event_date_end\n" +
                "FROM users \n" +
                "JOIN animals ON users.id = animals.user_id\n" +
                "JOIN kinds ON animals.kind_id = kinds.id\n" +
                "JOIN events ON animals.id = events.animal_id\n" +
                "WHERE users.id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, HelloApplication.idUser);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    String animal_name = resultSet.getString("animal_name");
                    String event_name = resultSet.getString("event_name");
                    java.util.Date date_start = resultSet.getDate("event_date_start");
                    java.util.Date date_end = resultSet.getDate("event_date_end");


                    ZonedDateTime timeStart = ZonedDateTime.of(
                            date_start.getYear() + 1900,
                            date_start.getMonth() + 1,
                            date_start.getDate(),
                            0,
                            0,
                            0,
                            0,
                             dateFocus.getZone()
                    );

                    ZonedDateTime timeEnd = ZonedDateTime.of(
                            date_end.getYear() + 1900,
                            date_end.getMonth() + 1,
                            date_end.getDate(),
                            0,
                            0,
                            0,
                            0,
                            dateFocus.getZone()
                    );

                    if (
                            dateFocus.getYear() == (date_start.getYear() + 1900) &&
                                    dateFocus.getMonthValue() == (date_start.getMonth() + 1)
                    ) {
                        calendarActivities.add(new CalendarActivity(timeStart, event_name, animal_name, true));
                    }

                    if (
                            dateFocus.getYear() == (date_end.getYear() + 1900) &&
                                    dateFocus.getMonthValue() == (date_end.getMonth() + 1)
                    ) {
                        calendarActivities.add(new CalendarActivity(timeEnd, event_name, animal_name, false));
                    }

                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        return createCalendarMap(calendarActivities);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    public ObservableList<Pills> getPills() {
        setConnection();

        ObservableList<Pills> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String queryKind = "SELECT id FROM kinds WHERE kind = ?";
        String queryPrescribing = "SELECT id FROM prescribing WHERE prescribing = ?";
        String queryPills = "SELECT * FROM pils WHERE kind_id = ? AND prescribing_id = ?";

        int kind_id = 0;
        int prescribing_id = 0;

        try (PreparedStatement pst = connection.prepareStatement(queryKind)) {
            pst.setString(1, HelloApplication.kindAnimal);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    kind_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(queryPrescribing)) {
            pst.setString(1, HelloApplication.prescribing);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    prescribing_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(queryPills)) {
            pst.setInt(1, kind_id);
            pst.setInt(2, prescribing_id);
            resultSet = pst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("pills res empty");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("pills res empty");
                alert.show();
            } else {
                System.out.println("pills not empty");
            }

            try {
                while (resultSet.next()) {
                    Pills pills =
                            new Pills(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("days")
                            );
                    data.add(pills);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);

        }
        return data;

    }

    public ObservableList<String> getPrescribing() {
        setConnection();

        ObservableList<String> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM prescribing";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    data.add(resultSet.getString("prescribing"));
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
        return data;
    }
}

