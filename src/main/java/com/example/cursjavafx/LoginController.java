package com.example.cursjavafx;

import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {
    public TextField login;
    public TextField password;
    private String regexPassword = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";
    private String regexLogin = "^[a-zA-Z][a-zA-Z0-9-_\\.]{4,20}$";
    PostgreDB db = PostgreDB.singleBD;
    @FXML
    protected void switchToRegPage(ActionEvent event) {
        Scenes.REGISTRATION.switchScene(event);
    }

    public void login(ActionEvent event) {
        if (Pattern.matches(regexPassword, password.getText()) && Pattern.matches(regexLogin, login.getText())) {
            db.loginUser(login.getText(), password.getText(), event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Некорректный логин или пароль");
            alert.show();
        }
    }
}
