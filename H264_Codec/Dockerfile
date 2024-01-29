# FROM openjdk:17
# COPY target/H264_Codec-1.0-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

FROM debian:bullseye

# Install OpenJDK 17 and ffmpeg
RUN apt update --fix-missing && \
    apt install -y openjdk-17-jre ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# Copy the JAR file and input.mp4
COPY target/H264_Codec-1.0-SNAPSHOT.jar app.jar
COPY input.mp4 input.mp4

# Set the entry point
ENTRYPOINT ["java", "-jar", "/app.jar"]
