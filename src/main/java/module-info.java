module com.example.cursjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.bootstrapfx.core;


    opens com.example.cursjavafx.database to javafx.fxml;
    exports com.example.cursjavafx.database;

    exports com.example.cursjavafx.utils;
    opens com.example.cursjavafx.utils to javafx.fxml;

    exports com.example.cursjavafx;
    opens com.example.cursjavafx to javafx.fxml;
    exports com.example.cursjavafx.classes;
    opens com.example.cursjavafx.classes to javafx.fxml;


}
