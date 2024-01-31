FROM debian:bullseye

# Install OpenJDK 17 and ffmpeg
RUN apt update --fix-missing && \
    apt install -y openjdk-17-jre ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# Install RabbitMQ
RUN apt-get update && \
    apt-get install -y --no-install-recommends rabbitmq-server && \
    rm -rf /var/lib/apt/lists/*

COPY entry_point.sh entry_point.sh
RUN tr -d '\r' < entry_point.sh > /tmp/entry_point.sh && mv /tmp/entry_point.sh /entry_point.sh
RUN chmod +x entry_point.sh

COPY target/H264_Codec-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["/entry_point.sh"]