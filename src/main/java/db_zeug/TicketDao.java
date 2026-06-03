package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.Ticket;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketDao {

    // Erstellt einen neuen Ort in der Datenbank
    public boolean create(Ticket ticket) {
        String sql = "INSERT INTO ticket (verursacher, grund) VALUES (?, ?)";
        // mach() liefert die Anzahl geänderter Zeilen oder -1 bei Fehlern
        int ergebnis = (int) SqlMacher.machUndHolId(sql, ticket.getBetroffen().getMitarbeiterId(), ticket.getVorfall());
        int fehler = 0;
        for (Mitarbeiter m : ticket.getAusteller()) {
            sql = "INSERT INTO ticket_austeller (ticket_id,aussteller_id) VALUES (? , ?)";
            int tmp = SqlMacher.mach(sql, ergebnis, m.getMitarbeiterId());
            if (tmp < 0) {
                fehler++;
            }
        }
        return fehler < 1;
    }

    // Liest einen einzelnen Ort anhand seiner ID aus
    public Ticket readOne(int ticketId) {
        String sql = "SELECT verursacher, id, zeitpunkt, grund FROM ticket WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, ticketId);
        sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung " +
                "FROM ticket_aussteller ta " +
                "LEFT JOIN mitarbeiter m ON ta.aussteller_id = m.id " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressort r ON m.ressort_id = r.id" +
                "WHERE ticket_id = ?";

        List<Map<String, Object>> aussteller = SqlMacher.such(sql, ticketId);
        if (ergebnis.isEmpty()) {
            return null;
        }

        return stuffToTicket(ergebnis.getFirst(), aussteller);
    }

    // Gibt alle Orte aus der Datenbank zurück
    public List<Ticket> readAll() {
        String sql = "SELECT verursacher, id, zeitpunkt, grund FROM ticket";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        List<Ticket> ticket = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung " +
                    "FROM ticket_aussteller ta " +
                    "LEFT JOIN mitarbeiter m ON ta.aussteller_id = m.id " +
                    "LEFT JOIN orte o ON m.ort_id = o.id " +
                    "LEFT JOIN ressort r ON m.ressort_id = r.id" +
                    "WHERE ticket_id = ?";

            List<Map<String, Object>> aussteller = SqlMacher.such(sql, (Integer) zeile.get("id"));
            ticket.add(stuffToTicket(zeile, aussteller));
        }
        return ticket;
    }

    // Aktualisiert ein Ticket und  löscht/erstellt entsprechende datensätze in ticket_aussteller (hoffentlich...)
    public boolean update(Ticket ticket) {
        int ticketId = ticket.getTicketId();
        String sql = "UPDATE ticket SET verursacher_id = ?, grund = ?, zeitpunkt = ? WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, ticket.getBetroffen().getMitarbeiterId(), ticket.getVorfall(), ticket.getZeitpunkt(), ticket.getTicketId());
        String sqlDeleteAll = "DELETE FROM ticket_aussteller WHERE ticket_id = ?";
        SqlMacher.mach(sqlDeleteAll, ticketId);
        String sqlInsert = "INSERT INTO ticket_aussteller (aussteller_id, ticket_id) VALUES (?, ?)";
        for (Mitarbeiter neu : ticket.getAusteller()) {
            SqlMacher.mach(sqlInsert, neu.getMitarbeiterId(), ticketId);
        }

        return ergebnis > 0;
    }

    // Löscht einen Ort anhand seiner ID
    public boolean delete(int ticketId) {
        String sql = "DELETE FROM ticket WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, ticketId);
        return ergebnis > 0;
    }

    // Entscheidet anhand der Existenz in der DB, ob create oder update gerufen wird
    public boolean save(Ticket ticket) {
        Ticket tucket = readOne(ticket.getTicketId());
        if (tucket == null) {
            return create(ticket);
        } else if (ticket.equals(tucket)) {
            return true;
        } else {
            return update(ticket);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Ort-Objekt um
    private Ticket stuffToTicket(Map<String, Object> zeile, List<Map<String, Object>> aussteller) {
        MitarbeiterDao b = new MitarbeiterDao();
        List<Mitarbeiter> ausstellerMA = new ArrayList<>();
        for (Map<String, Object> m : aussteller) {
            ausstellerMA.add(b.mapToMitarbeiter(m));
        }
        return new Ticket(b.readOneById((Integer) zeile.get("verursacher_id")), ausstellerMA.toArray(new Mitarbeiter[0]), (String) zeile.get("grund"), (Timestamp) zeile.get("zeitpunkt"));
    }
}


