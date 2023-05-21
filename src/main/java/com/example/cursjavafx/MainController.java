package com.example.cursjavafx;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.utils.Scenes;
import com.example.cursjavafx.database.PostgreDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
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
    private ObservableList<Animal> data = FXCollections.observableArrayList();
    PostgreDB db = new PostgreDB();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        animalName.setCellValueFactory(new PropertyValueFactory<>("name"));
        animalKind.setCellValueFactory(new PropertyValueFactory<>("kind"));
        animalDateB.setCellValueFactory(new PropertyValueFactory<>("date_birth"));

        animalTable.setItems(db.getAnimals());
    }

    public void openAddAnimal(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.ADD_ANIMAL.getTitle()));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void delete(ActionEvent event) {
        Animal animal = animalTable.getSelectionModel().getSelectedItems().get(0);
       System.out.println( );
       db.deleteAnimal(animal.getId(), event);
    }
}
