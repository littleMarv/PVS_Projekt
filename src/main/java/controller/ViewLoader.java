package controller;

import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import launcher.HelloApplication;

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

    public void ladeMitarbeiterDetails(Mitarbeiter mitarbeiter, AnchorPane targetContentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/mitarbeiter_view.fxml"));
            Pane detailView = loader.load();

            // Daten an den neuen Controller übergeben
            MitarbeiterViewController detailController = loader.getController();
            detailController.setAktuellerMitarbeiter(mitarbeiter);

            // Wichtig für das Layout (Wachstum erlauben)
            detailView.setMaxWidth(Double.MAX_VALUE);
            detailView.setMaxHeight(Double.MAX_VALUE);

            // In die übergebene Pane setzen und verankern
            targetContentPane.getChildren().setAll(detailView);
            AnchorPane.setTopAnchor(detailView, 0.0);
            AnchorPane.setRightAnchor(detailView, 0.0);
            AnchorPane.setBottomAnchor(detailView, 0.0);
            AnchorPane.setLeftAnchor(detailView, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ladeProjektDetails(Projekt projekt, AnchorPane targetContentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/projekt_view.fxml"));
            Pane detailView = loader.load();

            // Daten an den neuen Controller übergeben
            ProjektViewController detailController = loader.getController();
            detailController.setProjekt(projekt);

            // Wichtig für das Layout (Wachstum erlauben)
            detailView.setMaxWidth(Double.MAX_VALUE);
            detailView.setMaxHeight(Double.MAX_VALUE);

            // In die übergebene Pane setzen und verankern
            targetContentPane.getChildren().setAll(detailView);
            AnchorPane.setTopAnchor(detailView, 0.0);
            AnchorPane.setRightAnchor(detailView, 0.0);
            AnchorPane.setBottomAnchor(detailView, 0.0);
            AnchorPane.setLeftAnchor(detailView, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HelloApplication.class.getResource("/pvs_projekt/main_view.fxml")
            );
            fxmlLoader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}