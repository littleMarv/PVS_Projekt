package fachklassen;

import java.sql.Timestamp;

public class Ticket {
    private Mitarbeiter betroffen;
    private Mitarbeiter[] austeller;
    private int ticketId;
    private String vorfall;
    private Timestamp zeitpunkt;
}
