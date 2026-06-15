package db_zeug;

public class DatabaseConnectionException extends RuntimeException {
    // Reicht die Fehlermeldung und die Ursache (SQLException) nach oben durch
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}