package glim.antony.tutorial3.publish.subscribe;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/*
 *
 * @author a.yatsenko
 * Created at 22.04.2020
 *
 * @see {https://www.rabbitmq.com/tutorials/tutorial-three-java.html}
 */
public class Producer {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            for (int i = 0; i < 10; i++) {
                String message = "Hello World! " + i;
                channel.basicPublish(
                        EXCHANGE_NAME,
                        "",
                        null,
                        message.getBytes(StandardCharsets.UTF_8)
                );
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}
