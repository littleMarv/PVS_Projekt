package controller;

import db_zeug.TicketDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ticket;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TicketTableViewController implements Initializable {

    // Vollständige Liste aus der Datenbank
    private ObservableList<Ticket> alleTickets = FXCollections.observableArrayList();

    // Liste, die gerade in der Tabelle angezeigt wird
    private ObservableList<Ticket> angezeigteTickets = FXCollections.observableArrayList();

    @FXML
    private TextField ticketSucheTextField;

    @FXML
    private TableView<Ticket> ticketTableView;

    @FXML
    private TableColumn<Ticket, Integer> ticketIdColumn;

    @FXML
    private TableColumn<Ticket, String> betroffenColumn;

    @FXML
    private TableColumn<Ticket, String> grundColumn;

    @FXML
    private TableColumn<Ticket, String> datumColumn;

    @FXML
    private TableColumn<Ticket, String> uhrzeitColumn;

    @FXML
    private TableColumn<Ticket, String> ausstellerEinsColumn;

    @FXML
    private TableColumn<Ticket, String> ausstellerZweiColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Verbindet die Tabellenspalten mit den Ticketdaten
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        betroffenColumn.setCellValueFactory(zelle -> new SimpleStringProperty(mitarbeiterText(zelle.getValue().getBetroffen())));
        grundColumn.setCellValueFactory(zelle -> new SimpleStringProperty(zelle.getValue().getVorfall()));
        datumColumn.setCellValueFactory(zelle -> new SimpleStringProperty(datumText(zelle.getValue())));
        uhrzeitColumn.setCellValueFactory(zelle -> new SimpleStringProperty(uhrzeitText(zelle.getValue())));
        ausstellerEinsColumn.setCellValueFactory(zelle -> new SimpleStringProperty(ausstellerText(zelle.getValue(), 0)));
        ausstellerZweiColumn.setCellValueFactory(zelle -> new SimpleStringProperty(ausstellerText(zelle.getValue(), 1)));

        ladeTickets();
    }

    private void ladeTickets() {
        // Holt die Tickets über den DAO aus der Datenbank
        alleTickets.setAll(new TicketDao().readAll());
        angezeigteTickets.setAll(alleTickets);
        ticketTableView.setItems(angezeigteTickets);
    }

    @FXML
    void ticketSuche(KeyEvent event) {
        // Filtert in der bereits geladenen Liste, damit kein eigener DAO-Suchbefehl nötig ist
        String suche = ticketSucheTextField.getText().toLowerCase();
        angezeigteTickets.clear();

        for (Ticket ticket : alleTickets) {
            String suchZeile = (
                    ticket.getTicketId() + " "
                            + mitarbeiterText(ticket.getBetroffen()) + " "
                            + ticket.getVorfall() + " "
                            + datumText(ticket) + " "
                            + uhrzeitText(ticket) + " "
                            + ausstellerText(ticket, 0) + " "
                            + ausstellerText(ticket, 1)
            ).toLowerCase();

            if (suchZeile.contains(suche)) {
                angezeigteTickets.add(ticket);
            }
        }
    }

    @FXML
    void ticketNeuOeffnen() {
        zeigePlatzhalter("Ticket anlegen", "DB fehlt");
    }

    @FXML
    void ticketBearbeitenOeffnen() {
        Ticket ausgewaehltesTicket = ticketTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehltesTicket == null) {
            zeigePlatzhalter("Kein Ticket ausgewählt", "Bitte zuerst ein Ticket aus der Tabelle auswählen.");
            return;
        }

        zeigePlatzhalter("Ticket bearbeiten", "DB fehlt");
    }

    @FXML
    void ticketLoeschen() {
        Alert bestaetigung = new Alert(Alert.AlertType.CONFIRMATION);
        bestaetigung.setTitle("Ticket löschen");
        bestaetigung.setHeaderText(null);
        bestaetigung.setContentText("Soll das ausgewählte Ticket wirklich gelöscht werden?");

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");
        bestaetigung.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (bestaetigung.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
        zeigePlatzhalter("Löschen noch nicht verbunden", "DB fehlt");
        }
    }

    private String mitarbeiterText(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null) {
            return "-";
        }
        return mitarbeiter.getNachname() + ", " + mitarbeiter.getVorname();
    }

    private String ausstellerText(Ticket ticket, int index) {
        if (ticket.getAusteller() == null || ticket.getAusteller().length <= index) {
            return "-";
        }
        return mitarbeiterText(ticket.getAusteller()[index]);
    }

    private String datumText(Ticket ticket) {
        if (ticket.getZeitpunkt() == null) {
            return "-";
        }
        return ticket.getZeitpunkt().toLocalDateTime().toLocalDate().toString();
    }

    private String uhrzeitText(Ticket ticket) {
        if (ticket.getZeitpunkt() == null) {
            return "-";
        }
        return ticket.getZeitpunkt().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void zeigePlatzhalter(String titel, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
