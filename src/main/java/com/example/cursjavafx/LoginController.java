package com.example.cursjavafx;

import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    public TextField login;
    public TextField password;
    PostgreDB db;

    @FXML
    protected void switchToRegPage(ActionEvent event) {
        Scenes.REGISTRATION.switchScene(event);
    }

    public void login(ActionEvent event) {
        db = new PostgreDB();
        db.loginUser(login.getText(), password.getText(), event);
    }
}
