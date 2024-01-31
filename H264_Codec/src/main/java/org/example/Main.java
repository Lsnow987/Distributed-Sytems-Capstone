package org.example;

import java.io.*;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final String QUEUE_NAME = "video-processing-queue";
    public static void main(String[] args) {
        if (Files.exists(Paths.get("/home/lsnow/hello_world"))) {
            return;
        }
        // Connect to RabbitMQ and consume messages
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("docker-java-jar");
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
                processVideo(file);
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


    public static void processVideo(String file) {
        System.out.println(" [x] Received '" + file + "'");

//        get file name without path
        String fileName = file.substring(file.lastIndexOf('/') + 1);
//        remove extension
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        String commands = "# 240p\n" +
                "ls /home/lsnow/upload\n" +
                "mkdir -p videos/" + fileName + "/240p\n" +
                "ffmpeg -i " + file + " -c:v libx264 -b:v 400k -c:a aac -b:a 32k -threads 8 -g 128 -keyint_min 60 -s 426x240 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/" + fileName + "/240p/segment_%05d.ts videos/" + fileName + "/240p/playlist.m3u8\n" +
                "\n" +
                "# 480p\n" +
                "mkdir -p videos/" + fileName + "/480p\n" +
                "ffmpeg -i " + file + " -c:v libx264 -b:v 800k -c:a aac -b:a 64k -threads 8 -g 128 -keyint_min 60 -s 854x480 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/" + fileName + "/480p/segment_%05d.ts videos/" + fileName + "/480p/playlist.m3u8\n" +
                "\n" +
                "# 720p\n" +
                "mkdir -p videos/" + fileName + "/720p\n" +
                "ffmpeg -i " + file + " -c:v libx264 -b:v 1500k -c:a aac -b:a 128k -threads 8 -g 128 -keyint_min 60 -s 1280x720 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/" + fileName + "/720p/segment_%05d.ts videos/" + fileName + "/720p/playlist.m3u8\n" +
                "\n" +
                "# 1080p\n" +
                "mkdir -p videos/" + fileName + "/1080p\n" +
                "ffmpeg -i " + file + " -c:v libx264 -b:v 3000k -c:a aac -b:a 192k -threads 8 -g 128 -keyint_min 60 -s 1920x1080 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/" + fileName + "/1080p/segment_%05d.ts videos/" + fileName + "/1080p/playlist.m3u8\n" +
                "\n" +
                "# delete original file\n" +
                "rm " + file + "\n";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", commands);
            Process process = processBuilder.start();
            InputStream errorStream = process.getErrorStream();
            InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
            BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);
            String line2;
            while ((line2 = errorBufferedReader.readLine()) != null) {
                System.err.println(line2);
            }
            process.waitFor();
            createMasterPlaylist(fileName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute ffmpeg command: " +  e);
        }
        System.out.println(" [x] Done");
    }

    public static void createMasterPlaylist(String fileName) {
        try (FileWriter writer = new FileWriter("videos/" + fileName + "/master_playlist.m3u8")) {
            writer.write("#EXTM3U\n");
            writer.write("#EXT-X-VERSION:3\n");

            // Add entries for each resolution
            addEntry(writer, "240p", "240p/playlist.m3u8");
            addEntry(writer, "480p", "480p/playlist.m3u8");
            addEntry(writer, "720p", "720p/playlist.m3u8");
            addEntry(writer, "1080p", "1080p/playlist.m3u8");
            System.out.println("Master playlist created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addEntry(FileWriter writer, String resolution, String playlistPath) throws IOException {
        writer.write(String.format("#EXT-X-STREAM-INF:BANDWIDTH=%s,RESOLUTION=%s\n", getBandwidth(resolution), resolution));
        writer.write(playlistPath + "\n");
    }

    private static String getBandwidth(String resolution) {
        switch (resolution) {
            case "240p":
                return "400000";
            case "480p":
                return "800000";
            case "720p":
                return "1500000";
            case "1080p":
                return "3000000";
            default:
                return "0";
        }
    }
}
