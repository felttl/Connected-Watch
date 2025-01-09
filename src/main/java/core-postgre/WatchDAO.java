
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

    public WatchDAO(){
        id = new char[34];
    }

    public int add(WatchDAO watchdao) {
        var sql = "INSERT INTO products(name, price) "
                + "VALUES(?,?)";

        try (conn =  watchdao.connect();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // bind the values
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());

            // execute the INSERT statement and get the inserted id
            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}




