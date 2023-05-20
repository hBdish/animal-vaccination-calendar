module com.example.cursjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.cursjavafx to javafx.fxml;
    exports com.example.cursjavafx;
}
