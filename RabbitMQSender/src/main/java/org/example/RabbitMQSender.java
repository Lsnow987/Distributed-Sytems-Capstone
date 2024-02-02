//docker image build -t docker-rabbitmq:latest .
// docker run docker-rabbitmq:latest
//docker run --name docker-java-jar-latest --network mynetwork --hostname docker-java-jar -p 5672:5672 -p 15672:15672 docker-java-jar:latest
// docker run --name docker-java-jar-latest --network mynetwork --hostname docker-java-jar -p 5672:5672 -p 15672:15672 -v /home/lsnow/upload:/home/lsnow/upload -v /home/lsnow/videos:/videos docker-java-jar:latest
//  docker run --name my-rabbitmq-container --network mynetwork docker-rabbitmq:latest
package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQSender {
    private static final String QUEUE_NAME = "video-processing-queue";
    private static final String MESSAGE = "/home/lsnow/upload/input1.mp4";
    private static final int DELAY_SECONDS = 30;

    public static void main(String[] args) {
        // Connect to RabbitMQ and send a message
        if (Files.exists(Paths.get("/home/lsnow/hello_world"))) {
            return;
        }
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        factory.setHost("172.18.0.2");
//        factory.setHost("docker-java-jar");
        factory.setHost("rabbitmq-service");
        factory.setPort(5672);
        factory.setUsername("lsnow");
        factory.setPassword("shaarei1234");

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            System.out.println(" [*] Sending message every " + DELAY_SECONDS + " seconds. To exit press CTRL+C");

            // Declare the queue
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Schedule the message sending task
            executorService.scheduleAtFixedRate(() -> sendMessage(channel), 0, DELAY_SECONDS, TimeUnit.SECONDS);

            // Keep the main thread running to handle user interruption (CTRL+C)
            Thread.currentThread().join();
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the executor service when done
            executorService.shutdown();
        }
    }

    private static void sendMessage(Channel channel) {
        try {
            // Send the message to the queue
            channel.basicPublish("", QUEUE_NAME, null, MESSAGE.getBytes());
            System.out.println(" [x] Sent '" + MESSAGE + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
