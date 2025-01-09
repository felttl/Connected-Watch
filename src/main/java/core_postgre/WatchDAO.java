import java.util.UUID;

package core_JDK;
package core_postgre;

public class WatchDAO {

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

    private Connection connection;
    private static final String dbTable = "Watch";

    public WatchDAO(Watch watch){
        // add connection JDBC
        this.connection = (new Connection()).getConnection();
    }

    public int add(Watch watch) {
        // préparation
        String sql = "INSERT INTO " + Watch.dbTable + " (id, ER, Temp) VALUES (?, ?, ?)";
        this.connection = Connection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(
            sql, Statement.RETURN_GENERATED_KEYS
        );        
        pstmt.setInt(1, watch.getID());          
        pstmt.setInt(2, watch.getHeartRate());  
        pstmt.setDouble(3, watch.getTemp());    
        int insertedRows = pstmt.executeUpdate();
        // Vérifier si une ligne a été insérée et récupérer l'ID généré
        if (insertedRows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
}




