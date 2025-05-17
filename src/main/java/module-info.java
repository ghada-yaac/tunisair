module com.example.tunisair {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // si tu utilises SQL

    exports Controller;  // <-- exporte ce package à javafx.fxml

    opens Controller to javafx.fxml;  // <-- autorise l’accès de réflexion pour javafx.fxml

    exports app;
    opens app to javafx.fxml;
}
