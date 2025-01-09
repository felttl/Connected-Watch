import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connexion avec Singleton à une base de données PostgreSQL
 */
public class PostgreConnection {

    // Instance unique de PostgreConnection
    private static PostgreConnection instance;
    private Connection connection;

    // URL de connexion
    private final String url = "jdbc:postgresql://localhost:5432/postgreSQL";
    private final String username = "root";
    private final String password = "root";

    // Constructeur privé pour empêcher l'instanciation multiple
    private PostgreConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    // Méthode pour obtenir l'instance unique
    public static PostgreConnection getInstance() {
        if (instance == null) {
            synchronized (PostgreConnection.class) {
                if (instance == null) { // Double-checked locking pour éviter des problèmes de threads
                    instance = new PostgreConnection();
                }
            }
        }
        return instance;
    }

    // Méthode pour obtenir la connexion
    public Connection getConnection() {
        return connection;
    }
}
