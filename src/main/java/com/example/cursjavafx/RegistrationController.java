package com.example.cursjavafx;

import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrationController {
    public TextField name;
    public TextField login;
    public TextField password;
    PostgreDB db;

    public void onMainButtonClick(ActionEvent event) {
        db = new PostgreDB();
        db.createUser(name.getText(), login.getText(), password.getText());
    }

    public void switchToLoginPage(ActionEvent event) {
        try {
            new SceneController().switchScene(event, Scenes.LOGIN.getTitle());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
