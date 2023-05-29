package com.example.cursjavafx.utils;

import com.example.cursjavafx.SceneController;
import javafx.event.ActionEvent;

import java.io.IOException;

public enum Scenes {

    /**
     * Список всех окон
     */
    LOGIN ("login-view.fxml"),
    MAIN("main-view.fxml"),
    ADD_ANIMAL("addAnimal.fxml"),
    ANIMAL_EVENTS("animal-view.fxml"),
    CALENDAR("calendar.fxml"),
    REGISTRATION ("registration-view.fxml");

    private String title;

    Scenes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Функция смены окна
     * @param event событие нажатия на кнопку
     */
    public void switchScene(ActionEvent event) {
        try {
            new SceneController().switchScene(event, this.title);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
