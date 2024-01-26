package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
//        https://chat.openai.com/share/b8da0309-7b0a-4231-9dbe-415198dc8cb1 - explanation of commands
        String commands = "# 240p\n" +
                "mkdir -p videos/240p\n" +
                "ffmpeg -i input.mp4 -c:v libvpx-vp9 -b:v 400k -maxrate 500k -bufsize 1000k -c:a libopus -b:a 64k -threads 8 -tile-columns 2 -frame-parallel 1 -g 128 -keyint_min 60 -s 426x240 -profile:v 0 -f webm -dash 1 -slices 4 -map 0 -f segment -segment_time 10 -segment_format webm -segment_list videos/240p/playlist.m3u8 -segment_list_flags +live -segment_list_type m3u8 -segment_list_size 720 -segment_list_entry_prefix videos/240p/segment_ videos/240p/segment_%05d.webm\n" +
                "\n" +
                "# 480p\n" +
                "mkdir -p videos/480p\n" +
                "ffmpeg -i input.mp4 -c:v libvpx-vp9 -b:v 800k -maxrate 1000k -bufsize 2000k -c:a libopus -b:a 64k -threads 8 -tile-columns 2 -frame-parallel 1 -g 128 -keyint_min 60 -s 854x480 -profile:v 0 -f webm -dash 1 -slices 4 -map 0 -f segment -segment_time 10 -segment_format webm -segment_list videos/480p/playlist.m3u8 -segment_list_flags +live -segment_list_type m3u8 -segment_list_size 720 -segment_list_entry_prefix videos/480p/segment_ videos/480p/segment_%05d.webm\n" +
                "\n" +
                "# 720p\n" +
                "mkdir -p videos/720p\n" +
                "ffmpeg -i input.mp4 -c:v libvpx-vp9 -b:v 1500k -maxrate 2000k -bufsize 4000k -c:a libopus -b:a 128k -threads 8 -tile-columns 4 -frame-parallel 1 -g 128 -keyint_min 60 -s 1280x720 -profile:v 0 -f webm -dash 1 -slices 4 -map 0 -f segment -segment_time 10 -segment_format webm -segment_list videos/720p/playlist.m3u8 -segment_list_flags +live -segment_list_type m3u8 -segment_list_size 720 -segment_list_entry_prefix videos/720p/segment_ videos/720p/segment_%05d.webm\n" +
                "\n" +
                "# 1080p\n" +
                "mkdir -p videos/1080p\n" +
                "ffmpeg -i input.mp4 -c:v libvpx-vp9 -b:v 3000k -maxrate 4000k -bufsize 8000k -c:a libopus -b:a 128k -threads 8 -tile-columns 4 -frame-parallel 1 -g 128 -keyint_min 60 -s 1920x1080 -profile:v 0 -f webm -dash 1 -slices 4 -map 0 -f segment -segment_time 10 -segment_format webm -segment_list videos/1080p/playlist.m3u8 -segment_list_flags +live -segment_list_type m3u8 -segment_list_size 720 -segment_list_entry_prefix videos/1080p/segment_ videos/1080p/segment_%05d.webm";

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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
