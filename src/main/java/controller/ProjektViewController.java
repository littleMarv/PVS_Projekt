package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class ProjektViewController {

    // Projektleitungen dürfen wegen der Historie nicht gelöscht werden.
    @FXML
    void besetzungEntfernen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText("Projektleitung kann nicht gelöscht werden.");
        alert.showAndWait();
    }
}