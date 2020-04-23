package glim.antony.tutorial4.routing;

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

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String[] severityArray = {"info", "warning", "error"};

            for (int i = 0; i < 10; i++) {
                String message = "Hello World! " + i;
                for (String severity : severityArray) {
                    channel.basicPublish(
                            EXCHANGE_NAME,
                            severity,
                            null,
                            message.getBytes(StandardCharsets.UTF_8)
                    );
                }
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}
