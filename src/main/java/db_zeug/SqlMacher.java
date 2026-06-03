package db_zeug;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlMacher {
    // für Select
    public static List<Map<String,Object>> such(String sql, Object... parameter) {

        List<Map<String,Object>> ergebnisListe = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parameter.length; i++) {
                stmt.setObject(i + 1, parameter[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int spaltenAnzahl = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> zeile = new HashMap<>();
                    for (int i = 1; i <= spaltenAnzahl; i++) {
                        String spaltenName = metaData.getColumnName(i);
                        Object wert = rs.getObject(i);
                        zeile.put(spaltenName, wert);
                    }
                    ergebnisListe.add(zeile);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ergebnisListe;
    }


    // Führt ein INSERT, UPDATE oder DELETE sicher aus
    public static int mach(String sql, Object... parameter) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameter.length; i++) {
                stmt.setObject(i + 1, parameter[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long machUndHolId(String sql, Object... parameter) {
        // Hier wird explizit das Flag für die Keys übergeben
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < parameter.length; i++) {
                stmt.setObject(i + 1, parameter[i]);
            }

            stmt.executeUpdate();

            // Die generierte ID auslesen
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Gibt die neue ID zurück
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

