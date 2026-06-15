package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.TicketDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ticket;
import fachklassen.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TicketViewController implements Initializable {

    Ticket ticket;
    @FXML
    private SearchableComboBox<Mitarbeiter> ausstellerEinsComboBox;

    @FXML
    private SearchableComboBox<Mitarbeiter> ausstellerZweiComboBox;

    @FXML
    private SearchableComboBox<Mitarbeiter> betroffenComboBox;

    @FXML
    private DatePicker datumDatePicker;

    @FXML
    private TextField grundTextField;

    @FXML
    private Button ticketSpeichernButton;

    @FXML
    private SearchableComboBox<String> uhrzeitComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Mitarbeiter> alleMitarbeiter = List.of(new MitarbeiterDao().readAll());
        List<Mitarbeiter> ordnungsamt = alleMitarbeiter.stream()
                .filter(mitarbeiter -> mitarbeiter.getRessort() != null && "Ordnungsamt".equals(mitarbeiter.getRessort().getBezeichnung()))
                .toList();
        betroffenComboBox.getItems().setAll(alleMitarbeiter);
        ausstellerEinsComboBox.getItems().setAll(ordnungsamt);
        ausstellerZweiComboBox.getItems().setAll(ordnungsamt);
        List<String> zeiten = new ArrayList<>();
        for (int j = 0; j < 24; j++) {
            for (int i = 0; i < 60; i++) {
                zeiten.add(String.format("%02d", j) + ":" + String.format("%02d", i));
            }
        }
        this.ticket = new Ticket();
        uhrzeitComboBox.getItems().setAll(zeiten);
        betroffenComboBox.setConverter(new StringConverter<Mitarbeiter>() {
            @Override
            public String toString(Mitarbeiter m) {
                return m != null ? m.getAuswahlString() : "";
            }

            @Override
            public Mitarbeiter fromString(String s) {
                return null;
            }
        });
        ausstellerEinsComboBox.setConverter(new StringConverter<Mitarbeiter>() {
            @Override
            public String toString(Mitarbeiter m) {
                return m != null ? m.getAuswahlString() : "";
            }

            @Override
            public Mitarbeiter fromString(String s) {
                return null;
            }
        });
        ausstellerZweiComboBox.setConverter(new StringConverter<Mitarbeiter>() {
            @Override
            public String toString(Mitarbeiter m) {
                return m != null ? m.getAuswahlString() : "";
            }

            @Override
            public Mitarbeiter fromString(String s) {
                return null;
            }
        });

    }

    @FXML
    public void ticketSpeichern() {
        Mitarbeiter[] a = {ausstellerEinsComboBox.getValue(), ausstellerZweiComboBox.getValue()};
        this.ticket.setAusteller(a);
        this.ticket.setBetroffen(betroffenComboBox.getValue());
        this.ticket.setZeitpunkt(java.sql.Timestamp.valueOf(LocalDateTime.of(datumDatePicker.getValue(), LocalTime.parse(uhrzeitComboBox.getValue()))));
        this.ticket.setVorfall(grundTextField.getText());
        new TicketDao().save(this.ticket);
        AnchorPane hauptContentPane = (AnchorPane) betroffenComboBox.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            Pane ortTabelle = new ViewLoader().loadView("ticket_table_view",hauptContentPane);
        }
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        betroffenComboBox.setValue(this.ticket.getBetroffen());
        if (ticket.getAusteller() != null) {
            ausstellerEinsComboBox.setValue(this.ticket.getAusteller()[0]);
            ausstellerZweiComboBox.setValue(this.ticket.getAusteller()[1]);
        }
        else if (UserSession.getInstance()!= null && UserSession.getInstance().getUser() != null){
            ausstellerEinsComboBox.setValue(UserSession.getInstance().getUser().getMitarbeiter());
        }
        grundTextField.setText(this.ticket.getVorfall());
        if (ticket.getZeitpunkt() != null) {
            datumDatePicker.setValue(this.ticket.getDate());
            uhrzeitComboBox.setValue(this.ticket.getTime());
        }
        else {
            datumDatePicker.setValue(LocalDate.now());
            uhrzeitComboBox.setValue(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }
}