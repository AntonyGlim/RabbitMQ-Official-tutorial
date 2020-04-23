package glim.antony.tutorial5.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/*
 *
 * @author a.yatsenko
 * Created at 22.04.2020
 *
 * @see {https://www.rabbitmq.com/tutorials/tutorial-three-java.html}
 */
public class Producer {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.TOPIC;


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            String routingKey = "kern.critical";

            for (int i = 0; i < 10; i++) {
                String message = "Hello World! " + i;
                channel.basicPublish(
                        EXCHANGE_NAME,
                        routingKey,
                        null,
                        message.getBytes(StandardCharsets.UTF_8)
                );
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
        }
    }

}
