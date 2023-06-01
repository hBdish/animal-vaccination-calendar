package com.example.cursjavafx.utils;

import com.example.cursjavafx.SceneController;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Перечисление Scenes содержит названия всех сцен в приложении и используется для переключения между ними.
 * Каждый элемент перечисления представляет собой название файла FXML для соответствующей сцены.
 * Метод getTitle() возвращает название файла FXML для данной сцены.
 * Метод switchScene(ActionEvent event) вызывает метод switchScene() класса SceneController,
 * который переключает текущую сцену на сцену, соответствующую данному элементу перечисления.
 * Если произошла ошибка во время переключения сцен, то будет выведено сообщение об ошибке в консоль.
 */
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
