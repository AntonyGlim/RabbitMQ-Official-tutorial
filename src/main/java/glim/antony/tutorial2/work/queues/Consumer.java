package glim.antony.tutorial2.work.queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/*
 *
 * @author a.yatsenko
 * Created at 22.04.2020
 *
 * @see {https://www.rabbitmq.com/tutorials/tutorial-two-java.html}
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        boolean durable = true; //messages will be safe when RabbitMQ crashes
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages...");

        int prefetchCount = 1;
        channel.basicQos(prefetchCount); // accept only one unack-ed message at a time

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            doWork();
            System.out.println(" [x] Done");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); //send a proper acknowledgment from the worker, once we're done with a task
        };
        boolean autoAck = false; // "true" means that manual message acknowledgments are turned off. (By default it turned on and autoAck = false)
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
    }

    private static void doWork() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
