module org.example.sistemasicve {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.sistemasicve to javafx.fxml;
    opens app to javafx.fxml;
    exports org.example.sistemasicve;
}