package fachklassen;

import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Projekt {
    private int projektId;
    private String bezeichnung;
    private Date beginn;
    private Date abschluss;

    private List<ProjektMitarbeiter> mitarbeiterListe;
    private ProjektMitarbeiter projektLeitung;

    private List<ProjektMitarbeiter> mitarbeiterListetoAdd = new ArrayList<>();
    private List<ProjektMitarbeiter> mitarbeiterListetoDel = new ArrayList<>();

    public Projekt() {
    }

    ;

    public Projekt(String bezeichnung, Date beginn, Date abschluss, List<ProjektMitarbeiter> mitarbeiterListe) {
        this.bezeichnung = bezeichnung;
        this.beginn = beginn;
        this.abschluss = abschluss;
        this.mitarbeiterListe = mitarbeiterListe;
        for (ProjektMitarbeiter m : mitarbeiterListe) {
            if (Objects.equals(m.getRolle(), "Projektleitung") &&
                    (m.getBisDatum() == null || m.getBisDatum().toLocalDate().isAfter(LocalDate.now())) &&
                    !m.getVonDatum().toLocalDate().isAfter(LocalDate.now())) {
                this.projektLeitung = m;
            }
        }
    }

    public Projekt(int projektId, String bezeichnung, Date beginn, Date abschluss, List<ProjektMitarbeiter> mitarbeiterListe) {
        this.projektId = projektId;
        this.bezeichnung = bezeichnung;
        this.beginn = beginn;
        this.abschluss = abschluss;
        this.mitarbeiterListe = mitarbeiterListe;
        for (ProjektMitarbeiter m : mitarbeiterListe) {
            if (Objects.equals(m.getRolle(), "Projektleitung") &&
                    (m.getBisDatum() == null || m.getBisDatum().toLocalDate().isAfter(LocalDate.now())) &&
                    !m.getVonDatum().toLocalDate().isAfter(LocalDate.now())) {
                this.projektLeitung = m;
            }
        }
    }

    public int getProjektId() {
        return projektId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Date getBeginn() {
        return beginn;
    }

    public void setBeginn(Date beginn) {
        this.beginn = beginn;
    }

    public Date getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(Date abschluss) {
        this.abschluss = abschluss;
    }

    public List<ProjektMitarbeiter> getMitarbeiterListe() {
        return mitarbeiterListe;
    }

    public void setMitarbeiterListe(List<ProjektMitarbeiter> mitarbeiterListe) {
        this.mitarbeiterListe = mitarbeiterListe;
    }

    public List<ProjektMitarbeiter> getMitarbeiterListetoDel() {
        return mitarbeiterListetoDel;
    }

    public List<ProjektMitarbeiter> getMitarbeiterListetoAdd() {
        return mitarbeiterListetoAdd;
    }

    public ProjektMitarbeiter getProjektleitung() {
        return projektLeitung;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Projekt projekt = (Projekt) o;
        return projektId == projekt.projektId && Objects.equals(bezeichnung, projekt.bezeichnung) && Objects.equals(beginn, projekt.beginn) && Objects.equals(abschluss, projekt.abschluss) && Objects.equals(mitarbeiterListe, projekt.mitarbeiterListe) && Objects.equals(projektLeitung, projekt.projektLeitung) && Objects.equals(mitarbeiterListetoAdd, projekt.mitarbeiterListetoAdd) && Objects.equals(mitarbeiterListetoDel, projekt.mitarbeiterListetoDel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projektId, bezeichnung, beginn, abschluss, mitarbeiterListe, projektLeitung, mitarbeiterListetoAdd, mitarbeiterListetoDel);
    }

    public boolean add(ProjektMitarbeiter pm) {
        boolean check = true;
        if (Objects.equals(pm.getRolle(), "Projektleitung")) {
            for (ProjektMitarbeiter tmp : mitarbeiterListe) {
                if (Objects.equals(tmp.getRolle(), "Projektleitung"))
                    if(tmp.getVonDatum().after(pm.getVonDatum())&&tmp.getBisDatum().before(pm.getBisDatum())||tmp.getVonDatum().before(pm.getVonDatum())&&tmp.getBisDatum().after(pm.getBisDatum())||tmp.getVonDatum().before(pm.getBisDatum())&&tmp.getBisDatum().after(pm.getBisDatum())){
                        check = false;
                    }
            }
        }
        if (Objects.equals(pm.getRolle(), "Projektleitung")) {
            for (ProjektMitarbeiter tmp : mitarbeiterListetoAdd) {
                if (Objects.equals(tmp.getRolle(), "Projektleitung"))
                    if(tmp.getVonDatum().after(pm.getVonDatum())&&tmp.getBisDatum().before(pm.getBisDatum())||tmp.getVonDatum().before(pm.getVonDatum())&&tmp.getBisDatum().after(pm.getBisDatum())||tmp.getVonDatum().before(pm.getBisDatum())&&tmp.getBisDatum().after(pm.getBisDatum())){
                        check = false;
                    }
            }
        }
        if (check) {
            mitarbeiterListetoAdd.add(pm);
        }
        return check;
    }
}
