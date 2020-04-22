package glim.antony.tutorial2.work.queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;

/*
 *
 * @author a.yatsenko
 * Created at 22.04.2020
 *
 * @see {https://www.rabbitmq.com/tutorials/tutorial-two-java.html}
 */
public class Producer {

    private final static String QUEUE_NAME = "hello2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            boolean durable = true; //messages will be safe when RabbitMQ crashes
            channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

            for (int i = 0; i < 10; i++) {
                String message = "Hello World! " + i;
                channel.basicPublish(
                        "",
                        QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN, //it tells RabbitMQ to save the message to disk
                        message.getBytes(StandardCharsets.UTF_8)
                );
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}
