import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.ArrayList;
import java.util.List;

public class Monitor {

    private static final String EXCHANGE_NAME = "logs";
    private static final String BROKER_HOST = System.getenv("broker_host");

    // Liste pour stocker les objets DataPoint
    private static final List<DataPoint> dataPoints = new ArrayList<>();

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

            // Parse et ajoute le message dans la liste
            try {
                DataPoint dataPoint = parseMessage(message); // Utilisation de parseMessage
                dataPoints.add(dataPoint);
                System.out.println(" [x] Added DataPoint: " + dataPoint);
            } catch (Exception e) {
                System.err.println(" [!] Failed to parse message: " + message);
            }
        };
		var length = dataPoints.size();
		for (int i = 0; i<length; i++){

		}

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    // Méthode pour parser un message en un DataPoint
    private static DataPoint parseMessage(String message) {
        try {
            message = message.replace("{", "").replace("}", "").replace("\"", "");
            String[] parts = message.split(", ");
            String id = parts[0].split(": ")[1];
            double EC = Double.parseDouble(parts[1].split(": ")[1]);
            int temp = Integer.parseInt(parts[2].split(": ")[1]);

            return new DataPoint(id, EC, temp);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }

    // Classe interne représentant un point de données
    static class DataPoint {
        private final String ID;
        private final double EC;
        private final int Temp;

        public DataPoint(String id, double EC, int temp) {
            this.ID = id;
            this.EC = EC;
            this.Temp = temp;
        }

		public String getID(){
			return this.ID;
		}

		public double getFC(){
		return this.EC;
		}

		public int getTemp(){
			return this.Temp;
		}

        @Override
        public String toString() {
            return "{ID=" + ID + ", EC=" + EC + ", Temp=" + Temp + "}";
        }
    }
}
