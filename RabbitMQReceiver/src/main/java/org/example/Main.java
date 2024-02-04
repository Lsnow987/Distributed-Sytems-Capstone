package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String QUEUE_NAME = "video-processing-queue";
    private static int i = 0;
    public static void main(String[] args) {
        if (Files.exists(Paths.get("/home/lsnow/hello_world"))) {
            return;
        }
        // Connect to RabbitMQ and consume messages
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq-service");
        factory.setPort(5672);
        factory.setUsername("lsnow");
        factory.setPassword("shaarei1234");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // Declare the queue
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Create a consumer and set up the callback to handle messages
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String file = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + file + "'");
//                save info received in a file to be processed later
                try {
                    Files.write(Paths.get("/home/lsnow/video_to_process/input" + i), file.getBytes());
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            // Start consuming messages from the queue
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

            // Keep the program running until interrupted
            while (true) {
                Thread.sleep(1000);
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}