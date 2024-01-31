# mvn clean package
# docker image build -t docker-java-jar:latest .
# docker run -v /home/lsnow/upload:/home/lsnow/upload -v /home/lsnow/videos:/videos docker-java-jar:latest
# cp input1.mp4 /home/lsnow/upload/
#  cp -r /home/lsnow/videos/input1/ .

#!/bin/bash

# Set the path to your video processing directory
VIDEO_DIR=/home/lsnow/upload

# Find the newly uploaded video files
find "$VIDEO_DIR" -type f -name "*.mp4" | while read -r video_file; do

    # Run the app.jar within the Docker container
    java -jar app.jar "$video_file" 

    # Remove the input file
    rm "$video_file"
    
done
