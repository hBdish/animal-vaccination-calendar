package com.example.cursjavafx.database;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.HelloApplication;
import com.example.cursjavafx.SceneController;
import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.utils.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

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
                        try {
                            new SceneController().switchScene(event, Scenes.MAIN.getTitle());
                        } catch (IOException error) {
                            error.printStackTrace();
                        }
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                new SceneController().switchScene(event, Scenes.MAIN.getTitle());
            } catch (IOException error) {
                error.printStackTrace();
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                new SceneController().switchScene(event, Scenes.MAIN.getTitle());
            } catch (IOException error) {
                error.printStackTrace();
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
}

