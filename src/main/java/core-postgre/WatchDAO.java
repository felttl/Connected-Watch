import java.util.UUID;

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


    private char[] id;
    private Double heartRate;
    private int temp;

    private Connection connection;

    private static final dbTable = "Watch";

    public WatchDAO(){

        // add connection JDBC
        this.connection = (new Connection()).getConnection();
    }

    public int add(Watch watchdao) {
        // préparation
        String sql = "INSERT INTO " + WatchDAO.dbTable + " (id, ER, Temp) VALUES (?, ?, ?)";
        this.connction = Connection.getConnection();
        pstmt.setInt(1, watchdao.id);          
        pstmt.setInt(2, watchdao.heartRate);  
        pstmt.setDouble(3, watchdao.temp);    
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




