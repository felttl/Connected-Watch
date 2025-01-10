package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

    private static MysqlConnection instance = null;
    private Connection connection;

    // Constructeur privé pour empêcher l'instanciation multiple
    private MysqlConnection() {
        String url = "jdbc:mysql://mysql-container:3306/MontreConnectee";
        String username = "root";
        String password = "root";
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir l'instance Singleton
    public static Connection getConnection() {
        if (instance == null) {
            synchronized (MysqlConnection.class) {
                if (instance == null) {
                    instance = new MysqlConnection();
                }
            }
        }
        return MysqlConnection.instance.connection;
    }


    // Méthode pour fermer la connexion proprement
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
