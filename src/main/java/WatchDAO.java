package main.java;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class WatchDAO {

    private static final String EXCHANGE_NAME = "logs";
    private static final String BROKER_HOST = System.getenv("broker_host");

    /**
     * récupère les données et les transmet
     * fait aussi l'ajout dan la bd
     * @param argv
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("data saved '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            // on sauvegarde les données dans la db postgresql            
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
    }

    private java.sql.Connection connection;
    private static final String dbTable = "Watch";

    public WatchDAO(){
        // add connection JDBC
        this.connection = MysqlConnection.getConnection();
    }

    public int add(Watch watch) {
        // préparation
        int r=0;
        // on essaie avec mysql si erreur
        if(r==-1){
            String sql = "INSERT INTO " + dbTable + " (id, HR, Temp, wDate) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Remplir les paramètres de la requête
                pstmt.setString(1, watch.getId());
                pstmt.setDouble(2, watch.getHeartRate());
                pstmt.setDouble(3, watch.getTemp());
                pstmt.setString(4, watch.getDate());
                int insertedRows = pstmt.executeUpdate();
                if (insertedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
                r=0;
            } catch (SQLException e) {
                r=-1;
                System.err.println("Erreur lors de l'insertion des données avec MYSQL : " + e.getMessage());
            }
        }
        return r;
    }
}




    






