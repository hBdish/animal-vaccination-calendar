package com.example.cursjavafx;

import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.classes.Pills;
import com.example.cursjavafx.database.PostgreDB;
import com.example.cursjavafx.utils.Scenes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    @FXML
    public TableView<Pills> pillsTable;
    @FXML
    public TableColumn<Pills, Integer> id_pills;
    @FXML
    public TableColumn<Pills, String> name_pills;
    @FXML
    public TableColumn<Pills, Integer> days_pills;
    @FXML
    public ChoiceBox<String> prescribing;
    @FXML
    public DatePicker date_start_pills;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idLable.setText(HelloApplication.nameAnimal);
        ObservableList<String> dataPrescribing = PostgreDB.bd.getPrescribing();

        prescribing.setItems(dataPrescribing);
        prescribing.setValue(dataPrescribing.get(0));
        HelloApplication.prescribing = dataPrescribing.get(0);

        prescribing.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                HelloApplication.prescribing = prescribing.getItems().get((Integer) newValue);
                setTable(PostgreDB.bd);
            }
        });

        setTable(PostgreDB.bd);

    }

    public void setTable(PostgreDB db) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date_start.setCellValueFactory(new PropertyValueFactory<>("date_start"));
        date_end.setCellValueFactory(new PropertyValueFactory<>("date_end"));

        id_pills.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_pills.setCellValueFactory(new PropertyValueFactory<>("name"));
        days_pills.setCellValueFactory(new PropertyValueFactory<>("days"));

        eventTable.setItems(db.getEvents());
        pillsTable.setItems(db.getPills());
    }

    public void addEvent(ActionEvent event) {
        PostgreDB.bd.createEvent(name_event.getText(), date_start_event.getValue(), date_end_event.getValue(), event);
        System.out.println(HelloApplication.idAnimal);
    }

    public void deleteEvent(ActionEvent event) {
        EventsAnimals eventsAnimals = eventTable.getSelectionModel().getSelectedItem();
        PostgreDB.bd.deleteEvent(eventsAnimals.getId(), event);
    }

    public void switchToMain(ActionEvent event) {
        Scenes.MAIN.switchScene(event);
    }


    public void addPills(ActionEvent event) {
        Pills pills = pillsTable.getSelectionModel().getSelectedItem();
        LocalDate dateStart = date_start_pills.getValue();
        LocalDate dateEnd = calcDateEnd(dateStart, pills.getDays());
        PostgreDB.bd.createEvent(pills.getName(), dateStart, dateEnd, event);

    }

    private LocalDate calcDateEnd(LocalDate dateStart ,int days) {
        Date date = new Date(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        date.setDate(date.getDate() + days);
        LocalDate dateEnd = LocalDate.of(date.getYear(), date.getMonth() + 1, date.getDay() + 16);

        return dateEnd;
    }
}
