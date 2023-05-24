package com.example.cursjavafx;

import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.database.PostgreDB;
import com.example.cursjavafx.utils.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {
    public Text year;
    public Text month;
    PostgreDB db = new PostgreDB();

    ZonedDateTime dateFocus;
    ZonedDateTime today;
//    @FXML
//    public Label month;
//    @FXML
//    public Label year;
    @FXML
    public FlowPane calendar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    public void backOnMounth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void forwardOnMounth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Map<Integer, List<CalendarActivity>> calendarActivityMap = db.getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();

        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(
                dateFocus.getYear(),
                dateFocus.getMonthValue(),
                1,
                0,
                0,
                0,
                0, dateFocus.getZone()
        ).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH ;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() &&
                            today.getMonth() == dateFocus.getMonth() &&
                            today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }

    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    System.out.println(calendarActivities);
                });
                break;
            }

            String eventInfo = calendarActivities.get(k).getEventInfo() ? "День начала" : "День окончания";
            Text text = new Text(
                    calendarActivities.get(k).getClientName() + "\n" +
                    eventInfo + "\n" +
                    calendarActivities.get(k).getEvent_name()
            );

            if (calendarActivities.get(k).getEventInfo()) {
                calendarActivityBox.setStyle("-fx-background-color:GREEN");
            } else {
                calendarActivityBox.setStyle("-fx-background-color:RED");
            }

            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                System.out.println(text.getText());
            });
            calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
            calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
            calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        }
        stackPane.getChildren().add(calendarActivityBox);

    }

    public void backToMain(ActionEvent event) {
        Scenes.MAIN.switchScene(event);
    }
}


