package com.example.cursjavafx.database;


import com.example.cursjavafx.SceneController;
import com.example.cursjavafx.data.Scenes;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreDB {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/java";
    static final String USER = "postgres";
    static final String PASS = "1";
    private Connection connection = null;

//    public PostgreDB() {
//        System.out.println("Testing connection to PostgreSQL JDBC");
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
//            e.printStackTrace();
//            return;
//        }
//        System.out.println("PostgreSQL JDBC Driver successfully connected");
//
//        try {
//            this.connection = DriverManager
//                    .getConnection(DB_URL, USER, PASS);
//        } catch (SQLException e) {
//            System.out.println("Connection Failed");
//            e.printStackTrace();
//            return;
//        }
//        if (connection != null) {
//            System.out.println("You successfully connected to database now");
//        } else {
//            System.out.println("Failed to make connection to database");
//        }
//    }


    public void createUser(String name, String login, String password) {
        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        String query = "INSERT INTO users(name, login, password) VALUES(?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, login);
            pst.setString(3, password);
            pst.executeUpdate();
            System.out.println("success created user");

        } catch (SQLException error){
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
        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

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



        } catch (SQLException error){
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
}
