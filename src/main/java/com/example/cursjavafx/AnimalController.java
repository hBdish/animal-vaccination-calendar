package com.example.cursjavafx;

import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.database.PostgreDB;
import com.example.cursjavafx.utils.Scenes;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AnimalController implements Initializable {
    @FXML
    public TableView<EventsAnimals> eventTable;
    @FXML
    public TableColumn<EventsAnimals, Integer> id;
    @FXML
    public TableColumn<EventsAnimals, String> name;
    @FXML
    public TableColumn<EventsAnimals, Date> date_start;
    @FXML
    public TableColumn<EventsAnimals, Date> date_end;
    @FXML
    public Label idLable;
    @FXML
    public TextField name_event;
    @FXML
    public DatePicker date_start_event;
    @FXML
    public DatePicker date_end_event;
    PostgreDB db = new PostgreDB();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idLable.setText(HelloApplication.nameAnimal);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date_start.setCellValueFactory(new PropertyValueFactory<>("date_start"));
        date_end.setCellValueFactory(new PropertyValueFactory<>("date_end"));

        eventTable.setItems(db.getEvents());
    }

    public void addEvent(ActionEvent event) {
        db.createEvent(name_event.getText(), date_start_event.getValue(), date_end_event.getValue(), event);
        System.out.println(HelloApplication.idAnimal);
    }

    public void deleteEvent(ActionEvent event) {
        EventsAnimals eventsAnimals = eventTable.getSelectionModel().getSelectedItem();
        db.deleteEvent(eventsAnimals.getId(), event);
    }

    public void switchToMain(ActionEvent event) {
        try {
            new SceneController().switchScene(event, Scenes.MAIN.getTitle());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
