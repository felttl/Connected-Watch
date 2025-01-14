package main.java;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

/**
 * Connexion avec Singleton à une base de données PostgreSQL
 */
public class PostgreConnection {

    private static PostgreConnection instance;
    private Connection connection;

    // Constructeur privé pour empêcher l'instanciation multiple
    private PostgreConnection() {
        String url = "jdbc:postgres://postgres:5432/MontreConnectee";
        String username = "root";
        String password = "root";        
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (PSQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        } catch (SQLException e){
            System.err.println("Erreur de connexion : " + e.getMessage());            
        }
    }

    // Méthode pour obtenir l'instance unique
    public static Connection getConnection() {
        if (PostgreConnection.instance == null) {
            synchronized (PostgreConnection.class) {
                // Double-checked locking pour éviter des problèmes de threads
                if (PostgreConnection.instance == null) { 
                    PostgreConnection.instance = new PostgreConnection();
                }
            }
        }
        return PostgreConnection.instance.connection;
    }
}
