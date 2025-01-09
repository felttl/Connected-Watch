import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import main.java.*;

public class Producer {

	private static final String EXCHANGE_NAME = "logs";
	private static final String ROUTING_KEY = "#my_route";

	private static final String BROKER_HOST = System.getenv("broker_host");

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

			for (int i = 0; i < 10; i++) {

				Watch watch = new Watch();

				double EC = watch.getHeartRate(); 
				double Temp = watch.getTemp();
				String id = watch.getId();

				// Construction du message JSON
				String message = String.format("{\"ID\": \"%s\", \"EC\": %.2f, \"Temp\": %.2f}",id, EC, Temp);

				// Publication du message
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
				System.out.println(" [x] Sent: " + message);
			}
		}
	}
}
