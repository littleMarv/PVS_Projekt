package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class ViewLoader {

    // Lädt eine FXML-Datei aus dem Ressourcenordner pvs_projekt.
    public Pane loadView(String fileName) {
        try {
            URL fileUrl = MainViewController.class.getResource("/pvs_projekt/" + fileName + ".fxml");

            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML-Datei konnte nicht gefunden werden: " + fileName);
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            return loader.load();

        } catch (Exception e) {
            System.out.println("View konnte nicht geladen werden: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
}