package model;

import db_zeug.*;
import fachklassen.*;

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

    private final List<Mitarbeiter> alleMitarbeiter;
    private final List<Ort> alleOrte;
    private final List<Ressort> alleRessorts;
    private final List<Vertrag> alleVertraege;
    private final List<User> alleUser;
    private final List<UserRolle> alleUserRollen;
    private final List<Projekt> alleProjekte;
    private final List<Ticket> alleTickets;


    private Object focusObject;

    private ModelService() {
        alleMitarbeiter = mitarbeiterDao.readAll();
        alleOrte = ortDao.readAll();
        alleRessorts = ressortDao.readAll();
        alleVertraege = vertragDao.readAll();
        alleUser = userDao.readAll();
        alleUserRollen = userRollenDao.readAll();
        alleProjekte = projektDao.readAll();
        alleTickets = ticketDao.readAll();
    }

    public boolean updateModel() {
        try {
            updateMitarbeiter();

            alleRessorts.clear();
            alleRessorts.addAll(ressortDao.readAll());
            alleVertraege.clear();
            alleVertraege.addAll(vertragDao.readAll());
            alleUser.clear();
            alleUser.addAll(userDao.readAll());
            alleUserRollen.clear();
            alleUserRollen.addAll(userRollenDao.readAll());
            alleProjekte.clear();
            alleProjekte.addAll(projektDao.readAll());
            alleTickets.clear();
            alleTickets.addAll(ticketDao.readAll());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean updateMitarbeiter() {
        try{
            List<Mitarbeiter> tmp = mitarbeiterDao.readAll();
            alleMitarbeiter.clear();
            alleMitarbeiter.addAll(tmp);
        }
        catch (){return false;}

        alleMitarbeiter.addAll(mitarbeiterDao.readAll());
        return true;
    }

    public boolean updateOrte() {
        alleOrte.clear();
        alleOrte.addAll(ortDao.readAll());
        return true
    }

    public boolean updateRessorts() {
        alleRessorts.clear();
        alleRessorts.addAll(ressortDao.readAll());
        return true;
    }

    public boolean updateVertraege() {
        alleVertraege.clear();
        alleVertraege.addAll(vertragDao.readAll());
        return true;
    }

    public boolean updateUser() {
        alleUser.clear();
        alleUser.addAll(userDao.readAll());
        return true;
    }

    public boolean updateUserRollen() {
        alleUserRollen.clear();
        alleUserRollen.addAll(userRollenDao.readAll());
    }

    public boolean updateProjekte() {
        alleProjekte.clear();
        alleProjekte.addAll(projektDao.readAll());
    }

    public boolean updateTickets() {
        alleTickets.clear();
        alleTickets.addAll(ticketDao.readAll());
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

    public List<Mitarbeiter> getAlleMitarbeiter() {
        return alleMitarbeiter;
    }

    public List<Ort> getAlleOrte() {
        return alleOrte;
    }

    public List<Ressort> getAlleRessorts() {
        return alleRessorts;
    }

    public List<Vertrag> getAlleVertraege() {
        return alleVertraege;
    }

    public List<User> getAlleUser() {
        return alleUser;
    }

    public List<UserRolle> getAlleUserRollen() {
        return alleUserRollen;
    }

    public List<Projekt> getAlleProjekte() {
        return alleProjekte;
    }

    public List<Ticket> getAlleTickets() {
        return alleTickets;
    }


}


