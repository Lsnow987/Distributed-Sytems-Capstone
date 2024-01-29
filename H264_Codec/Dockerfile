# FROM openjdk:17
# COPY target/H264_Codec-1.0-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# command to get shell:
# docker run -it --entrypoint bash fe708710263f6f1c25ce02fe40811ed4e37a7a76ad68bdc757676f652f5b8da2
# java -jar app.jar
FROM debian:bullseye

# Install OpenJDK 17 and ffmpeg
RUN apt update --fix-missing && \
    apt install -y openjdk-17-jre ffmpeg && \
    rm -rf /var/lib/apt/lists/*

RUN apt-get update && \
    apt-get install -y --no-install-recommends inotify-tools && \
    rm -rf /var/lib/apt/lists/*


# Copy the JAR file and input.mp4
COPY target/H264_Codec-1.0-SNAPSHOT.jar app.jar
COPY input.mp4 input.mp4
COPY inotify_script.sh inotify_script.sh
COPY process_video.sh process_video.sh

# Set appropriate permissions
RUN chmod +x process_video.sh
RUN chmod +x inotify_script.sh

# Set the entry point
# ENTRYPOINT ["java", "-jar", "/app.jar"]
ENTRYPOINT ["/inotify_script.sh"]

