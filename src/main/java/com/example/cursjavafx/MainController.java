package com.example.cursjavafx;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public TableView<Animal> animalTable;
    @FXML
    public TableColumn<Animal, Integer> animalId;
    @FXML
    public TableColumn<Animal, String> animalName;
    @FXML
    public TableColumn<Animal, String> animalKind;
    @FXML
    public TableColumn<Animal, Date> animalDateB;
    PostgreDB db = PostgreDB.singleBD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        animalName.setCellValueFactory(new PropertyValueFactory<>("name"));
        animalKind.setCellValueFactory(new PropertyValueFactory<>("kind"));
        animalDateB.setCellValueFactory(new PropertyValueFactory<>("date_birth"));

        animalTable.setItems(db.getAnimals());
    }

    public void openAddAnimal(ActionEvent event) {
        Scenes.ADD_ANIMAL.switchScene(event);
    }

    public void delete(ActionEvent event) {
        Animal animal = animalTable.getSelectionModel().getSelectedItems().get(0);
        db.deleteAnimal(animal.getId(), event);
    }

    public void openAnimal(ActionEvent event) {
            Animal animal = animalTable.getSelectionModel().getSelectedItems().get(0);
            PostgreDB.idAnimal = animal.getId();
            PostgreDB.nameAnimal = animal.getName();
            PostgreDB.kindAnimal = animal.getKind();
            Scenes.ANIMAL_EVENTS.switchScene(event);
    }

    public void openCalendar(ActionEvent event) {
        Animal animal = animalTable.getSelectionModel().getSelectedItems().get(0);
        PostgreDB.idAnimal = animal.getId();
        PostgreDB.nameAnimal = animal.getName();
        PostgreDB.kindAnimal = animal.getKind();
        Scenes.CALENDAR.switchScene(event);
    }
}
