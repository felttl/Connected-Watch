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

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    // Méthode pour parser un message en un DataPoint
    private static DataPoint parseMessage(String message) {
        try {
            message = message.replace("{", "").replace("}", "").replace("\"", "");
            String[] parts = message.split(", ");
            String id = parts[0].split(": ")[1];
            double fc = Double.parseDouble(parts[1].split(": ")[1]);
            int temp = Integer.parseInt(parts[2].split(": ")[1]);

            return new DataPoint(id, fc, temp);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }

    // Classe interne représentant un point de données
    static class DataPoint {
        private final String ID;
        private final double FC;
        private final int Temp;

        public DataPoint(String id, double fc, int temp) {
            this.ID = id;
            this.FC = fc;
            this.Temp = temp;
        }

        @Override
        public String toString() {
            return "{ID=" + ID + ", FC=" + FC + ", Temp=" + Temp + "}";
        }
    }
}
