module org.example.sistemasicve {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;

    opens org.example.sistemasicve to javafx.fxml;
    exports org.example.sistemasicve;
}