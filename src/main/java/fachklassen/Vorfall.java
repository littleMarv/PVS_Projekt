package fachklassen;

import java.sql.Timestamp;

public class Vorfall {
    private Mitarbeiter betroffen;
    private Mitarbeiter[] zeugen;
    private int vorfall_id;
    private String grund;
    private Timestamp zeitpunkt;
}
