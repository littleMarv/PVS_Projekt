package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class ViewLoader {
    private Pane view;

    public Pane loadView(String fileName) {
        try {
            URL fileUrl = MainViewController.class.getResource("/package1/" + fileName + ".fxml");

            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML-Datei konnte nicht unter dem Pfad gefunden werden.");
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            view = loader.load();

        } catch (Exception e) {
            System.out.println("No page " + fileName + ". Please check FXMLLoader. Error: " + e.getMessage());
            e.printStackTrace();
        }
        return view;
    }
}