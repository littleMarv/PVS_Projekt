module org.example.pvs_projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports controller;
    opens controller to javafx.fxml;
    exports launcher;
    opens launcher to javafx.fxml;
}