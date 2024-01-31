FROM debian:bullseye

# Install OpenJDK 17 and ffmpeg
RUN apt update --fix-missing && \
    apt install -y openjdk-17-jre ffmpeg && \
    rm -rf /var/lib/apt/lists/*

RUN apt-get update && \
    apt-get install -y --no-install-recommends inotify-tools && \
    rm -rf /var/lib/apt/lists/*

# Install RabbitMQ
RUN apt-get update && \
    apt-get install -y --no-install-recommends rabbitmq-server && \
    rm -rf /var/lib/apt/lists/*

# intall aws cli
RUN apt-get update && \
    apt-get install -y --no-install-recommends awscli && \
    rm -rf /var/lib/apt/lists/*

# Copy the JAR file 
COPY target/H264_Codec-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY inotify_script.sh inotify_script.sh
COPY process_video.sh process_video.sh
COPY entry_point.sh entry_point.sh
RUN tr -d '\r' < entry_point.sh > /tmp/entry_point.sh && mv /tmp/entry_point.sh /entry_point.sh


# Set appropriate permissions
RUN chmod +x process_video.sh
RUN chmod +x inotify_script.sh
RUN chmod +x entry_point.sh

# Set the entry point
# ENTRYPOINT ["/inotify_script.sh"]
ENTRYPOINT ["/entry_point.sh"]
