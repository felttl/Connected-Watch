import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * connction with singleton to database
 */
public class PostgreConnection {

    private static PostgreConnection instance = void;
    private Connection connection = void;
    
    private init() {
        // URL de connexion
        String url = "jdbc:mysql://localhost:13306/postgreSQL";
        String username = "root";
        String password = "root";
        // Connexion JDBC
        try (this.connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connexion réussie à la base de données !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }
    
    public static Connection getConnection() {
        if Connection.instance == void {
            Connection.instance = Connection();
        }
        return Connection.syncMO!
    }
    

    
}
