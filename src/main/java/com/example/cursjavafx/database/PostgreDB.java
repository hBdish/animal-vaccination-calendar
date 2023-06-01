package com.example.cursjavafx.database;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.HelloApplication;
import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.classes.Pills;
import com.example.cursjavafx.utils.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс подключения к базе данных
 * <p>
 * Содержит:
 * Методы взаимодействия с базой данных
 */
public class PostgreDB implements dbDao {

    public static int idUser;
    public static int idAnimal;

    public static String nameAnimal;
    public static String kindAnimal;
    public static String prescribing;

    public static final PostgreDB singleBD = new PostgreDB();

    /**
     * строка подключения к серверу базы данных
     */
    static final String DB_URL = "jdbc:postgresql://localhost:5432/java";
    /**
     * строка с именем пользователя для подключения к серверу базы данных
     */
    static final String USER = "postgres";
    /**
     * строка с паролем для подключения к серверу базы данных
     */
    static final String PASS = "1";
    /**
     * поле содержащее сессию подключения к серверу базы данных
     */
    private Connection connection = null;

    /**
     * функция установки соединения с сервером базы данных
     */
    @Override
    public void setConnection() {
        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }

    /**
     * функция создания нового пользователя
     * @param name имя пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    @Override
    public void createUser(String name, String login, String password) {
        setConnection();
        String query = "INSERT INTO users(name, login, password) VALUES(?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, login);
            pst.setString(3, password);
            pst.executeUpdate();
            System.out.println("success created user");

        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param event событие нажатия на кнопку
     */
    @Override
    public void loginUser(String login, String password, ActionEvent event) {
        setConnection();

        String query = "SELECT * FROM users WHERE login = ? AND password = ?";
        ResultSet resultSet;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.setString(2, password);
            resultSet = pst.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Неверный логин или пароль");
                alert.show();
            } else {
                while (resultSet.next()) {
                    idUser = resultSet.getInt("id");
                    String log = resultSet.getString("login");
                    String pass = resultSet.getString("password");

                    if (log.equals(login) && pass.equals(password)) {
                        Scenes.MAIN.switchScene(event);
                    } else {
                        System.out.println("Login or password did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * функция получения списка животных
     * @return список животных
     */
    @Override
    public ObservableList<Animal> getAnimals() {
        setConnection();

        ObservableList<Animal> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM animals JOIN kinds ON animals.kind_id = kinds.id WHERE user_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idUser);
            resultSet = pst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("animal res empty");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("animal res empty");
                alert.show();
            } else {
                System.out.println("animal not empty");
            }

            try {
                while (resultSet.next()) {
                    Animal a =
                            new Animal(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("kind"),
                                    resultSet.getDate("date_birth")
                            );
                    data.add(a);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);

        }
        return data;
    }
    /**
     * функция получения списка видов животных
     * @return список видов животных
     */
    @Override
    public ObservableList<String> getKinds() {
        setConnection();

        ObservableList<String> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM kinds";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    data.add(resultSet.getString("kind"));
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
        return data;
    }

    /**
     * функция создания нового животного
     * @param name кличка животного
     * @param kind вид животного
     * @param date дата рождения животного
     * @param event событие нажатия на кнопку
     * @param reglament булевое значение, определяющее необходимость учета регламента при создании животного
     */
    @Override
    public void createAnimal(String name, String kind, LocalDate date, ActionEvent event, Boolean reglament) {
        setConnection();

        int kind_id = -1;
        ResultSet resultSet;

        String query = "SELECT id FROM kinds WHERE kind = ?";
        String query2 = "INSERT INTO animals(name, kind_id, date_birth, user_id) VALUES(?, ?, ?, ?)";
        String queryIdAnimal = "SELECT id FROM animals WHERE user_id = ? AND name = ? AND date_birth = ?";
        String queryReglament = "SELECT * FROM reglament WHERE kind_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, kind);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    kind_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(query2)) {
            pst.setString(1, name);
            pst.setInt(2, kind_id);
            pst.setDate(3, Date.valueOf(date));
            pst.setInt(4, PostgreDB.idUser);
            pst.executeUpdate();
            System.out.println("success created animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }finally {
            if (reglament) {

                try (PreparedStatement pst = connection.prepareStatement(queryIdAnimal)) {
                    pst.setInt(1, PostgreDB.idUser);
                    pst.setString(2, name);
                    pst.setDate(3, Date.valueOf(date));
                    resultSet = pst.executeQuery();

                    try {
                        while (resultSet.next()) {
                            idAnimal = resultSet.getInt("id");
                        }
                    } catch (SQLException error) {
                        System.out.println(error.getMessage());
                    }
                } catch (SQLException error) {
                    Logger logger = Logger.getLogger(PostgreDB.class.getName());
                    logger.log(Level.SEVERE, error.getMessage(), error);
                }


                try (PreparedStatement pst = connection.prepareStatement(queryReglament)) {
                    pst.setInt(1, kind_id);
                    resultSet = pst.executeQuery();

                    try {
                        while (resultSet.next()) {
                            System.out.println(resultSet.getString("name"));
                            createEventreglament(
                                    resultSet.getString("name"),
                                    calcDateEnd(date, resultSet.getInt("daysStart")),
                                    calcDateEnd(calcDateEnd(date, resultSet.getInt("daysStart")),resultSet.getInt("days"))
                            );
                        }
                    } catch (SQLException error) {
                        System.out.println(error.getMessage());
                    }

                } catch (SQLException error) {
                    Logger logger = Logger.getLogger(PostgreDB.class.getName());
                    logger.log(Level.SEVERE, error.getMessage(), error);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Scenes.MAIN.switchScene(event);
                }
            }
        }
    }

    /**
     * функция удаления животного
     * @param id id животного
     */
    @Override
    public void deleteAnimal(int id, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query = "DELETE FROM animals WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("delete animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
                Scenes.MAIN.switchScene(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * функция получения списка мероприятий
     * @return список мероприятий
     */
    @Override
    public ObservableList<EventsAnimals> getEvents() {
        setConnection();

        ObservableList<EventsAnimals> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM events WHERE animal_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idAnimal);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    EventsAnimals event =
                            new EventsAnimals(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getDate("date_start"),
                                    resultSet.getDate("date_end")
                            );
                    data.add(event);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
        return data;
    }

    /**
     * функция создания нового мероприятия
     * @param name название мероприятия
     * @param date_start дата начала мероприятия
     * @param date_end дата конца мероприятия
     * @param event событие нажатия на кнопку
     */
    @Override
    public void createEvent(String name, LocalDate date_start, LocalDate date_end, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query1 = "INSERT INTO events(name, date_start, date_end, animal_id) VALUES(?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query1)) {
            pst.setString(1, name);
            pst.setDate(2, Date.valueOf(date_start));
            pst.setDate(3, Date.valueOf(date_end));
            pst.setInt(4, idAnimal);
            pst.executeUpdate();
            System.out.println("success created event");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Scenes.ANIMAL_EVENTS.switchScene(event);
        }
    }

    /**
     * функция добавления мероприятия из регламента
     * @param name название мероприятия
     * @param date_start дата начала мероприятия
     * @param date_end дата конца мероприятия
     */
    @Override
    public void createEventreglament(String name, LocalDate date_start, LocalDate date_end) {

        ResultSet resultSet;
        String query1 = "INSERT INTO events(name, date_start, date_end, animal_id) VALUES(?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query1)) {
            pst.setString(1, name);
            pst.setDate(2, Date.valueOf(date_start));
            pst.setDate(3, Date.valueOf(date_end));
            pst.setInt(4, this.idAnimal);
            pst.executeUpdate();
            System.out.println("success created event");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
    }

    /**
     * функция удаления мероприятия
     * @param id id мероприятия
     */
    @Override
    public void deleteEvent(int id, ActionEvent event) {
        setConnection();

        ResultSet resultSet;
        String query = "DELETE FROM events WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("delete animal");
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        } finally {
            try {
                connection.close();
                Scenes.ANIMAL_EVENTS.switchScene(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * функция получения списка мероприятий в месяце
     * @param dateFocus дата
     * @return список мероприятий на месяц
     */
    @Override
    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        setConnection();

        List<CalendarActivity> calendarActivities = new ArrayList<>();
        ResultSet resultSet;

        String query = "SELECT animals.name as animal_name, events.name as event_name, events.date_start as event_date_start, events.date_end as event_date_end\n" +
                "FROM users \n" +
                "JOIN animals ON users.id = animals.user_id\n" +
                "JOIN kinds ON animals.kind_id = kinds.id\n" +
                "JOIN events ON animals.id = events.animal_id\n" +
                "WHERE users.id = ? AND animals.id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idUser);
            pst.setInt(2, idAnimal);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    String animal_name = resultSet.getString("animal_name");
                    String event_name = resultSet.getString("event_name");
                    java.util.Date date_start = resultSet.getDate("event_date_start");
                    java.util.Date date_end = resultSet.getDate("event_date_end");
                    System.out.println(animal_name);

                    ZonedDateTime timeStart = ZonedDateTime.of(
                            date_start.getYear() + 1900,
                            date_start.getMonth() + 1,
                            date_start.getDate(),
                            0,
                            0,
                            0,
                            0,
                             dateFocus.getZone()
                    );

                    ZonedDateTime timeEnd = ZonedDateTime.of(
                            date_end.getYear() + 1900,
                            date_end.getMonth() + 1,
                            date_end.getDate(),
                            0,
                            0,
                            0,
                            0,
                            dateFocus.getZone()
                    );

                    if (
                            dateFocus.getYear() == (date_start.getYear() + 1900) &&
                                    dateFocus.getMonthValue() == (date_start.getMonth() + 1)
                    ) {
                        calendarActivities.add(new CalendarActivity(timeStart, event_name, animal_name, true));
                    }

                    if (
                            dateFocus.getYear() == (date_end.getYear() + 1900) &&
                                    dateFocus.getMonthValue() == (date_end.getMonth() + 1)
                    ) {
                        calendarActivities.add(new CalendarActivity(timeEnd, event_name, animal_name, false));
                    }
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        return createCalendarMap(calendarActivities);
    }

    /**
     * функция создания списка мароприятий
     * @param calendarActivities календарь активностей на месяц
     * @return список мароприятий
     */
    @Override
    public Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);
                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    /**
     * функция получения списка мероприятий для определенного вида животных
     * @return список мероприятий для определенного вида животных
     */
    @Override
    public ObservableList<Pills> getPills() {
        setConnection();

        ObservableList<Pills> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String queryKind = "SELECT id FROM kinds WHERE kind = ?";
        String queryPrescribing = "SELECT id FROM prescribing WHERE prescribing = ?";
        String queryPills = "SELECT * FROM pils WHERE kind_id = ? AND prescribing_id = ?";

        int kind_id = 0;
        int prescribing_id = 0;

        try (PreparedStatement pst = connection.prepareStatement(queryKind)) {
            pst.setString(1, kindAnimal);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    kind_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(queryPrescribing)) {
            pst.setString(1, prescribing);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    prescribing_id = resultSet.getInt("id");
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }

        try (PreparedStatement pst = connection.prepareStatement(queryPills)) {
            pst.setInt(1, kind_id);
            pst.setInt(2, prescribing_id);
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    Pills pills =
                            new Pills(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("days")
                            );
                    data.add(pills);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);

        }
        return data;

    }

    /**
     * функция получения списка видов мероприятий
     * @return список видов мероприятий
     */
    @Override
    public ObservableList<String> getPrescribing() {
        setConnection();

        ObservableList<String> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        String query = "SELECT * FROM prescribing";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            resultSet = pst.executeQuery();

            try {
                while (resultSet.next()) {
                    data.add(resultSet.getString("prescribing"));
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(PostgreDB.class.getName());
            logger.log(Level.SEVERE, error.getMessage(), error);
        }
        return data;
    }

    /**
     * функция расчета дней
     * @param dateStart начальная дата
     * @param days количество прибавляемых дней
     * @return LocalDate
     */

    public LocalDate calcDateEnd(LocalDate dateStart, int days) {
        java.util.Date date = new java.util.Date(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth());
        date.setDate(date.getDate() + days);
        LocalDate dateEnd = LocalDate.of(date.getYear(), date.getMonth() + 1, date.getDay() + 16);

        return dateEnd;
    }
}

