FROM debian:bullseye

# RUN apt update --fix-missing && \
#     apt install -y openjdk-17-jre ffmpeg inotify-tools rabbitmq-server && \
#     rm -rf /var/lib/apt/lists/*
#     originally these 3:
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

# Copy the JAR file
COPY target/H264_Codec-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY entry_point.sh entry_point.sh
RUN tr -d '\r' < entry_point.sh > /tmp/entry_point.sh && mv /tmp/entry_point.sh /entry_point.sh

# Set appropriate permissions
RUN chmod +x entry_point.sh

# Set the entry point
ENTRYPOINT ["/entry_point.sh"]
