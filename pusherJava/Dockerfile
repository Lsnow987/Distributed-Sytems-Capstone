FROM openjdk:8-jdk-slim

# Install dependencies
RUN apt-get update && apt-get install -y inotify-tools awscli dos2unix maven

WORKDIR /app

# Copy your project and build it
COPY . /app
RUN mvn package

# New step: Copy the JAR to a different location
RUN mkdir /deployments && cp /app/target/s3uploader-1.0-SNAPSHOT.jar /deployments/

# Update permissions and the script as necessary
RUN chmod a+rx /deployments/s3uploader-1.0-SNAPSHOT.jar
RUN chmod +x inotify_script.sh

CMD ["./inotify_script.sh"]
