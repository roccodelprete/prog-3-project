module org.example.sistemasicve {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires jbcrypt;
    requires twilio;

    opens org.sistemasicve to javafx.fxml;
    exports command.pattern;
    opens app to javafx.fxml;
    opens command.pattern;
    opens strategy.pattern;
    opens utils;
    opens observer_memento.pattern;
    exports org.sistemasicve.admin;
    exports observer_memento.pattern;
    opens org.sistemasicve.admin to javafx.fxml;
    exports org.sistemasicve;
    exports org.sistemasicve.user;
    opens org.sistemasicve.user to javafx.fxml;
}