package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ModelService;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Lädt die Instanz und befüllt das Model (zeitaufwändig)
        Thread loadThread = new Thread(ModelService::getInstance);
        loadThread.start();
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/pvs_projekt/login_view.fxml")
        );

        // Erstellt die Szene mit Breite und Höhe
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        // Fenstertitel setzen
        stage.setTitle("PVS Projektverwaltung");

        // Szene dem Fenster zuweisen
        stage.setScene(scene);

        // Fenster anzeigen
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}