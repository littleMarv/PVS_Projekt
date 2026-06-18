package controller;

import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import fachklassen.Ticket;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import launcher.HelloApplication;
import model.ModelService;

import java.io.IOException;
import java.net.URL;

public class ViewLoader {
    static private ViewLoader vl;
    private MainViewController mvc;
    private AnchorPane targetContentPane;
    private ViewLoader(){}

    public static ViewLoader getViewLoader(){
        if (vl ==null){
            vl = new ViewLoader();
        }
        return vl;
    }
    public Pane loadView(String fileName, Object o){
        ModelService.getInstance().setFocusObject(o);
        return loadView(fileName);
    }
    // Lädt eine FXML-Datei aus dem Ressourcenordner pvs_projekt.
    public Pane loadView(String fileName) {
        try {
            URL fileUrl = MainViewController.class.getResource("/pvs_projekt/" + fileName + ".fxml");

            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML-Datei konnte nicht gefunden werden: " + fileName);
            }
            if (this.mvc != null) {
                this.mvc.markCat(fileName);
            }
            FXMLLoader loader = new FXMLLoader(fileUrl);
            Pane view = loader.load();

            // Wichtig für das Layout (Wachstum erlauben)
            view.setMaxWidth(Double.MAX_VALUE);
            view.setMaxHeight(Double.MAX_VALUE);

            // In die übergebene Pane setzen und verankern
            targetContentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);

            ModelService.getInstance().getVerlauf().add(fileName);
            return view;

        } catch (Exception e) {
            System.out.println("View konnte nicht geladen werden: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public void loadMain(Stage stage) {

        try {
            // 1. Die neue main_view.fxml laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/main_view.fxml"));
            Parent mainViewRoot = loader.load();
            this.mvc = loader.getController();
            this.targetContentPane = this.mvc.getContentPane();
            this.mvc.dashboardAnzeigen();
            // 3. Einfach die Root der vorhandenen Scene austauschen
            stage.getScene().setRoot(mainViewRoot);

            // Optional: Fenstergröße anpassen oder zentrieren, falls die main_view größer ist
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            // Hier evtl. eine Fehlermeldung für den User anzeigen
        }

    }


    // TODO: ab hier alte methoden die ersetzt werden sollen.

/*    public void ladeMitarbeiterDetails(Mitarbeiter mitarbeiter, AnchorPane targetContentPane) {
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



    public void ladeTicketDetails(Ticket ticket, AnchorPane targetContentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/ticket_view.fxml"));
            Pane detailView = loader.load();

            // Daten an den neuen Controller übergeben
            TicketViewController detailController = loader.getController();
            detailController.setTicket(ticket);

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
    }*/
}