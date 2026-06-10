package fachklassen;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

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

    public Ticket() {

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId == ticket.ticketId && Objects.equals(betroffen, ticket.betroffen) && Objects.deepEquals(austeller, ticket.austeller) && Objects.equals(vorfall, ticket.vorfall) && Objects.equals(zeitpunkt, ticket.zeitpunkt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, betroffen, Arrays.hashCode(austeller), vorfall, zeitpunkt);
    }
}
