package glim.antony.tutorial4.routing;

import com.rabbitmq.client.*;

/*
 *
 * @author a.yatsenko
 * Created at 22.04.2020
 *
 * @see {https://www.rabbitmq.com/tutorials/tutorial-three-java.html}
 */
public class Consumer {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

//        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
//        channel.queueBind(queueName, EXCHANGE_NAME, "error");

        System.out.println(" [*] Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        boolean autoAck = true; // "true" means that manual message acknowledgments are turned off. (By default it turned on and autoAck = false)
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }

}
