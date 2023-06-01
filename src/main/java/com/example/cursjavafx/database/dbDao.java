package com.example.cursjavafx.database;

import com.example.cursjavafx.classes.Animal;
import com.example.cursjavafx.classes.CalendarActivity;
import com.example.cursjavafx.classes.EventsAnimals;
import com.example.cursjavafx.classes.Pills;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface dbDao {

    /**
     * функция установки соединения с сервером базы данных
     */
    void setConnection();

    /**
     * функция создания нового пользователя
     * @param name имя пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    public void createUser(String name, String login, String password);

    /**
     * функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param event событие нажатия на кнопку
     */
    public void loginUser(String login, String password, ActionEvent event);

    /**
     * функция получения списка животных
     * @return список животных
     */
    public ObservableList<Animal> getAnimals();

    /**
     * функция получения списка видов животных
     * @return список видов животных
     */
    public ObservableList<String> getKinds();

    /**
     * функция создания нового животного
     * @param name кличка животного
     * @param kind вид животного
     * @param date дата рождения животного
     * @param event событие нажатия на кнопку
     * @param reglament булевое значение, определяющее необходимость учета регламента при создании животного
     */
    public void createAnimal(String name, String kind, LocalDate date, ActionEvent event, Boolean reglament);

    /**
     * функция удаления животного
     * @param id id животного
     * @param event событие нажатия на кнопку
     */
    public void deleteAnimal(int id, ActionEvent event);

    /**
     * функция получения списка мероприятий
     * @return список мероприятий
     */
    public ObservableList<EventsAnimals> getEvents();

    /**
     * функция создания нового мероприятия
     * @param name название мероприятия
     * @param date_start дата начала мероприятия
     * @param date_end дата конца мероприятия
     * @param event событие нажатия на кнопку
     */
    public void createEvent(String name, LocalDate date_start, LocalDate date_end, ActionEvent event);

    /**
     * функция добавления мероприятия из регламента
     * @param name название мероприятия
     * @param date_start дата начала мероприятия
     * @param date_end дата конца мероприятия
     */
    public void createEventreglament(String name, LocalDate date_start, LocalDate date_end);

    /**
     * функция удаления мероприятия
     * @param id id мероприятия
     * @param event событие нажатия на кнопку
     */
    public void deleteEvent(int id, ActionEvent event);

    /**
     * функция получения списка мероприятий в месяце
     * @param dateFocus дата
     * @return список мероприятий на месяц
     */
    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus);

    /**
     * функция создания списка мароприятий
     * @param calendarActivities календарь активностей на месяц
     * @return список мароприятий
     */
    Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities);

    /**
     * Метод getPills() получает список лекарств из базы данных по заданным параметрам (вид животного и назначение лекарства).
     * @return список лекарств в виде ObservableList
     */
    public ObservableList<Pills> getPills();

    /**
     * функция получения списка видов мероприятий
     * @return список видов мероприятий
     */
    public ObservableList<String> getPrescribing();

}
