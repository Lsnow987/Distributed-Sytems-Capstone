package org.example;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
//        using h264 codec - outdated - see here: https://techblog.skeepers.io/an-introduction-to-the-difficult-world-of-video-processing-c31642b9f806
        String commands = "# 240p\n" +
                "mkdir -p videos/240p\n" +
                "ffmpeg -i input.mp4 -c:v libx264 -b:v 400k -c:a aac -b:a 32k -threads 8 -g 128 -keyint_min 60 -s 426x240 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/240p/segment_%05d.ts videos/240p/playlist.m3u8\n" +
                "\n" +
                "# 480p\n" +
                "mkdir -p videos/480p\n" +
                "ffmpeg -i input.mp4 -c:v libx264 -b:v 800k -c:a aac -b:a 64k -threads 8 -g 128 -keyint_min 60 -s 854x480 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/480p/segment_%05d.ts videos/480p/playlist.m3u8\n" +
                "\n" +
                "# 720p\n" +
                "mkdir -p videos/720p\n" +
                "ffmpeg -i input.mp4 -c:v libx264 -b:v 1500k -c:a aac -b:a 128k -threads 8 -g 128 -keyint_min 60 -s 1280x720 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/720p/segment_%05d.ts videos/720p/playlist.m3u8\n" +
                "\n" +
                "# 1080p\n" +
                "mkdir -p videos/1080p\n" +
                "ffmpeg -i input.mp4 -c:v libx264 -b:v 3000k -c:a aac -b:a 192k -threads 8 -g 128 -keyint_min 60 -s 1920x1080 -hls_time 10 -hls_list_size 720 -hls_segment_filename videos/1080p/segment_%05d.ts videos/1080p/playlist.m3u8\n";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", commands);
            Process process = processBuilder.start();

            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Command exited with code: " + exitCode);

            // Create a master M3U8 file
            createMasterPlaylist();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void createMasterPlaylist() {
        try (FileWriter writer = new FileWriter("videos/master_playlist.m3u8")) {
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
