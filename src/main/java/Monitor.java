import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Monitor {

	private static final String EXCHANGE_NAME = "logs";

	private static final String BROKER_HOST = System.getenv("broker_host");

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}


static class DataPoint {
	private final String id; 
	private final double fc;  
	 

	public DataPoint(String id, double fc, int temp) {
		this.id = id;
		this.fc = fc;
		this.temp = temp;
	}

	@Override
	public String toString() {
		return "{ID=" + id + ", FC=" + fc + ", Temp=" + temp + "}";
	}
}
}
