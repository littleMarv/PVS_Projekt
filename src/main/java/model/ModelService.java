package model;

import db_zeug.*;
import fachklassen.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ModelService {
    private static ModelService instance;

    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
    private final OrtDao ortDao = new OrtDao();
    private final ProjektDao projektDao = new ProjektDao();
    private final RessortDao ressortDao = new RessortDao();
    private final TicketDao ticketDao = new TicketDao();
    private final UserDao userDao = new UserDao();
    private final UserRollenDao userRollenDao = new UserRollenDao();
    private final VertragDao vertragDao = new VertragDao();

    private final ObservableList<Mitarbeiter> alleMitarbeiter = FXCollections.observableArrayList();
    private final ObservableList<Ort> alleOrte = FXCollections.observableArrayList();
    private final ObservableList<Ressort> alleRessorts = FXCollections.observableArrayList();
    private final ObservableList<Vertrag> alleVertraege = FXCollections.observableArrayList();
    private final ObservableList<User> alleUser = FXCollections.observableArrayList();
    private final ObservableList<UserRolle> alleUserRollen = FXCollections.observableArrayList();
    private final ObservableList<Projekt> alleProjekte = FXCollections.observableArrayList();
    private final ObservableList<Ticket> alleTickets = FXCollections.observableArrayList();

    private Object focusTab;
    private Object focusObject;

    private final List<String> verlauf = new ArrayList<>();

    private ModelService() {
        updateModel();
    }

    public boolean updateModel() {
       return updateMitarbeiter()&
        updateOrte()&
        updateRessorts()&
        updateVertraege()&
        updateUser()&
        updateUserRollen()&
        updateProjekte()&
        updateTickets();
    }

    public boolean updateMitarbeiter() {
        List<Mitarbeiter> tmp;
        try {
            tmp = mitarbeiterDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleMitarbeiter.setAll(tmp);
        return true;
    }

    public boolean updateOrte() {
        List<Ort> tmp;
        try {
            tmp = ortDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleOrte.setAll(tmp);
        return true;
    }

    public boolean updateRessorts() {
        List<Ressort> tmp;
        try {
            tmp = ressortDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleRessorts.setAll(tmp);
        return true;
    }

    public boolean updateVertraege() {
        List<Vertrag> tmp;
        try {
            tmp = vertragDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleVertraege.setAll(tmp);
        return true;
    }

    public boolean updateUser() {
        List<User> tmp;
        try {
            tmp = userDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleUser.setAll(tmp);
        return true;
    }

    public boolean updateUserRollen() {
        List<UserRolle> tmp; // Typ ggf. anpassen, falls Ihre Klasse anders heißt
        try {
            tmp = userRollenDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleUserRollen.setAll(tmp);
        return true;
    }

    public boolean updateProjekte() {
        List<Projekt> tmp;
        try {
            tmp = projektDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleProjekte.setAll(tmp);
        return true;
    }

    public boolean updateTickets() {
        List<Ticket> tmp;
        try {
            tmp = ticketDao.readAll();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
        alleTickets.setAll(tmp);
        return true;
    }


    public static ModelService getInstance() {
        if (instance == null) {
            instance = new ModelService();
        }
        return instance;
    }

    public Object getFocusObject() {
        return focusObject;
    }

    public void setFocusObject(Object focusObject) {
        this.focusObject = focusObject;
    }

    public ObservableList<Mitarbeiter> getAlleMitarbeiter() {
        return alleMitarbeiter;
    }

    public ObservableList<Ort> getAlleOrte() {
        return alleOrte;
    }

    public ObservableList<Ressort> getAlleRessorts() {
        return alleRessorts;
    }

    public ObservableList<Vertrag> getAlleVertraege() {
        return alleVertraege;
    }

    public ObservableList<User> getAlleUser() {
        return alleUser;
    }

    public ObservableList<UserRolle> getAlleUserRollen() {
        return alleUserRollen;
    }

    public ObservableList<Projekt> getAlleProjekte() {
        return alleProjekte;
    }

    public ObservableList<Ticket> getAlleTickets() {
        return alleTickets;
    }

    public List<String> getVerlauf() {
        return verlauf;
    }
}


