package com.example.cursjavafx.data;

public enum Scenes {
    LOGIN ("login-view.fxml"),
    MAIN("main-view.fxml"),
    REGISTRATION ("registration-view.fxml");

    private String title;

    Scenes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
