import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.time.LocalDateTime;
import java.util.*;

import main.java.*;


public class Monitor {

    private static final String EXCHANGE_NAME = "logs";
    private static final String BROKER_HOST = System.getenv("broker_host");

    // Liste pour stocker les objets DataPoint
    private static final ArrayList<Watch> dataPoints = new ArrayList<>();

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
                Watch dataPoint = parseMessage(message); // Utilisation de parseMessage
                dataPoints.add(dataPoint);
                System.out.println(" [x] Added DataPoint: " + dataPoint);
            } catch (Exception e) {
                System.err.println(" [!] Failed to parse message: " + message);
            }
        };
		int length = dataPoints.size();
        WatchDAO watchDAO = new WatchDAO();
		for (int i = 0; i<length; i++){
            watchDAO.add(dataPoints.get(i));
            System.out.println("Ajout dans la table : "+dataPoints.get(i).toString());
		}

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    // Méthode pour parser un message en un DataPoint
    private static Watch parseMessage(String message) {
        try {
            message = message.replace("{", "").replace("}", "").replace("\"", "");
            String[] parts = message.split(", ");
            String id = parts[0].split(": ")[1];
            double EC = Double.parseDouble(parts[1].split(": ")[1]);
            int temp = Integer.parseInt(parts[2].split(": ")[1]);

            Watch uneWatch =  new Watch();

            uneWatch.setId(id);
            uneWatch.setHeartRate(EC);
            uneWatch.setTemp(temp);
        return uneWatch;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message, e);
        }
    }

  
}
