package fachklassen;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Ticket {
    private int ticketId;
    private Mitarbeiter betroffen;
    private Mitarbeiter[] austeller;
    private String vorfall;
    private Timestamp zeitpunkt;

    public Ticket(Mitarbeiter betroffen, Mitarbeiter[] austeller, String vorfall, Timestamp zeitpunkt) {
        this.betroffen = betroffen;
        this.austeller = austeller;
        this.vorfall = vorfall;
        this.zeitpunkt = zeitpunkt;
    }

    public Ticket(int ticketId, Mitarbeiter betroffen, Mitarbeiter[] austeller, String vorfall, Timestamp zeitpunkt) {
        this.ticketId = ticketId;
        this.betroffen = betroffen;
        this.austeller = austeller;
        this.vorfall = vorfall;
        this.zeitpunkt = zeitpunkt;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Mitarbeiter getBetroffen() {
        return betroffen;
    }

    public void setBetroffen(Mitarbeiter betroffen) {
        this.betroffen = betroffen;
    }

    public Mitarbeiter[] getAusteller() {
        return austeller;
    }

    public void setAusteller(Mitarbeiter[] austeller) {
        this.austeller = austeller;
    }

    public String getVorfall() {
        return vorfall;
    }

    public void setVorfall(String vorfall) {
        this.vorfall = vorfall;
    }

    public Timestamp getZeitpunkt() {
        return zeitpunkt;
    }

    public void setZeitpunkt(Timestamp zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    public LocalDate getDate(){
        return zeitpunkt.toLocalDateTime().toLocalDate();
    }

    public String getTime(){
        return zeitpunkt.toLocalDateTime().getHour()+":"+zeitpunkt.toLocalDateTime().getMinute();
    }
}
