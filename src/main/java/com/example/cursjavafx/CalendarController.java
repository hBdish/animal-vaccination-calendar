package com.example.cursjavafx;

import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.database.PostgreDB;
import com.example.cursjavafx.utils.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {
    public Text year;
    public Text month;
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    @FXML
    public FlowPane calendar;
    PostgreDB db = PostgreDB.singleBD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    public void backOnMounth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void forwardOnMounth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    /**
     * Создает календарь на основе текущей даты и добавляет его в соответствующий контейнер.
     */
    private void drawCalendar() {
        // Установка года и месяца в соответствующие элементы интерфейса
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        // Определение размеров и отступов для каждой ячейки календаря
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Получение списка событий для каждого дня месяца
        Map<Integer, List<CalendarActivity>> calendarActivityMap = db.getCalendarActivitiesMonth(dateFocus);

        // Определение количества дней в месяце
        int monthMaxDate = dateFocus.getMonth().maxLength();

        // Если год не високосный и февраль имеет 29 дней, то устанавливаем кол-во дней в 28
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        // Определение дня недели, на который приходится первый день месяца
        int dateOffset = ZonedDateTime.of(
                dateFocus.getYear(),
                dateFocus.getMonthValue(),
                1,
                0,
                0,
                0,
                0, dateFocus.getZone()
        ).getDayOfWeek().getValue();

        // Заполнение ячеек календаря днями месяца и событиями, если они есть
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

    /**
     * Creates a VBox containing information about calendar activities and adds it to the specified StackPane.
     *
     * @param calendarActivities список объектов CalendarActivity, содержащих информацию об отображаемых действиях.
     * @param rectangleHeight высота прямоугольника, в который будет помещен VBox
     * @param rectangleWidth ширина прямоугольника, в который будет помещен VBox
     * @param stackPane StackPane, к которому будет добавлен VBox
     */
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> System.out.println(calendarActivities));
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION, text.getText());
                alert.show();
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


