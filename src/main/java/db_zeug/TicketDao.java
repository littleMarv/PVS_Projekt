package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.Ticket;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketDao {

    // Erstellt ein neues Ticket in der Datenbank
    public boolean create(Ticket ticket) {
        String sql = "INSERT INTO ticket (verursacher_id, grund, zeitpunkt) VALUES (?, ?, ?)";
        int ticketId = (int) SqlMacher.machUndHolId(sql, ticket.getBetroffen().getMitarbeiterId(), ticket.getVorfall(), ticket.getZeitpunkt());

        int fehler = 0;
        for (Mitarbeiter mitarbeiter : ticket.getAusteller()) {
            sql = "INSERT INTO ticket_aussteller (ticket_id, aussteller_id) VALUES (?, ?)";
            int ergebnis = SqlMacher.mach(sql, ticketId, mitarbeiter.getMitarbeiterId());

            if (ergebnis < 0) {
                fehler++;
            }
        }

        return fehler < 1;
    }

    // Liest ein einzelnes Ticket anhand seiner ID aus
    public Ticket readOne(int ticketId) {
        String sql = "SELECT verursacher_id, id, zeitpunkt, grund FROM ticket WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, ticketId);

        if (ergebnis.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> aussteller = ladeAussteller(ticketId);
        return stuffToTicket(ergebnis.getFirst(), aussteller);
    }

    // Gibt alle Tickets aus der Datenbank zurück
    public List<Ticket> readAll() {
        String sql = "SELECT verursacher_id, id, zeitpunkt, grund FROM ticket";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        List<Ticket> tickets = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            List<Map<String, Object>> aussteller = ladeAussteller((Integer) zeile.get("id"));
            tickets.add(stuffToTicket(zeile, aussteller));
        }

        return tickets;
    }

    // Aktualisiert ein Ticket und setzt die Aussteller neu
    public boolean update(Ticket ticket) {
        int ticketId = ticket.getTicketId();
        String sql = "UPDATE ticket SET verursacher_id = ?, grund = ?, zeitpunkt = ? WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, ticket.getBetroffen().getMitarbeiterId(), ticket.getVorfall(), ticket.getZeitpunkt(), ticketId);

        String sqlDeleteAll = "DELETE FROM ticket_aussteller WHERE ticket_id = ?";
        SqlMacher.mach(sqlDeleteAll, ticketId);

        String sqlInsert = "INSERT INTO ticket_aussteller (ticket_id, aussteller_id) VALUES (?, ?)";
        for (Mitarbeiter mitarbeiter : ticket.getAusteller()) {
            SqlMacher.mach(sqlInsert, ticketId, mitarbeiter.getMitarbeiterId());
        }

        return ergebnis > 0;
    }

    // Löscht ein Ticket anhand seiner ID
    public boolean delete(int ticketId) {
        String sql = "DELETE FROM ticket WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, ticketId);
        return ergebnis > 0;
    }

    // Entscheidet anhand der Existenz in der DB, ob create oder update gerufen wird
    public boolean save(Ticket ticket) {
        Ticket vorhandenesTicket = readOne(ticket.getTicketId());

        if (vorhandenesTicket == null) {
            return create(ticket);
        } else if (ticket.equals(vorhandenesTicket)) {
            return true;
        } else {
            return update(ticket);
        }
    }

    private List<Map<String, Object>> ladeAussteller(int ticketId) {
        String sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung AS ressortbezeichnung, v.bezeichnung AS vertragbezeichnung " +
                "FROM ticket_aussteller ta " +
                "LEFT JOIN mitarbeiter m ON ta.aussteller_id = m.id " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id " +
                "WHERE ta.ticket_id = ?";

        return SqlMacher.such(sql, ticketId);
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Ticket-Objekt um
    private Ticket stuffToTicket(Map<String, Object> zeile, List<Map<String, Object>> aussteller) {
        MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
        List<Mitarbeiter> ausstellerListe = new ArrayList<>();

        for (Map<String, Object> mitarbeiterZeile : aussteller) {
            ausstellerListe.add(mitarbeiterDao.mapToMitarbeiter(mitarbeiterZeile));
        }

        Mitarbeiter betroffen = mitarbeiterDao.readOneById((Integer) zeile.get("verursacher_id"));
        return new Ticket((Integer) zeile.get("id"), betroffen, ausstellerListe.toArray(new Mitarbeiter[0]), (String) zeile.get("grund"), mapToTimestamp(zeile.get("zeitpunkt")));
    }

    private Timestamp mapToTimestamp(Object wert) {
        if (wert instanceof Timestamp timestamp) {
            return timestamp;
        }

        if (wert instanceof LocalDateTime localDateTime) {
            return Timestamp.valueOf(localDateTime);
        }

        return null;
    }
}
