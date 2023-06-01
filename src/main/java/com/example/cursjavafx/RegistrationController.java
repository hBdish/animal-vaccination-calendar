package com.example.cursjavafx;

import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegistrationController {
    public TextField name;
    public TextField login;
    public TextField password;
    private String regexPassword = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";
    private String regexLogin = "^[a-zA-Z][a-zA-Z0-9-_\\.]{4,20}$";
    PostgreDB db = PostgreDB.singleBD;
    public void onMainButtonClick(ActionEvent event) {

        if (Pattern.matches(regexPassword, password.getText()) && Pattern.matches(regexLogin, login.getText()) && name.getText().isEmpty()) {
            db.createUser(name.getText(), login.getText(), password.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Вы зарегистрировались!");
            alert.show();
            Scenes.LOGIN.switchScene(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Некорректный логин или пароль");
            alert.show();
        }

    }

    public void switchToLoginPage(ActionEvent event) {
        Scenes.LOGIN.switchScene(event);
    }
}
