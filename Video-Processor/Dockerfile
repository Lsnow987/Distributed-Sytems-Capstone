FROM debian:bullseye

RUN apt update --fix-missing && \
    apt install -y openjdk-17-jre ffmpeg inotify-tools && \
    rm -rf /var/lib/apt/lists/*

# Copy the JAR file 
COPY target/H264_Codec-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Copy the scripts
COPY process_video.sh process_video.sh
COPY entry_point.sh entry_point.sh
RUN tr -d '\r' < entry_point.sh > /tmp/entry_point.sh && mv /tmp/entry_point.sh /entry_point.sh

# Set appropriate permissions
RUN chmod +x process_video.sh
RUN chmod +x entry_point.sh

# Set the entry point
ENTRYPOINT ["/entry_point.sh"]
