#!/bin/bash

# Set the path to your Docker image
DOCKER_IMAGE=lsnow987/docker-java-jar:latest

# Set the path to your video processing directory
VIDEO_DIR=/home/lsnow/upload

OUTPUT_DIR=/mnt/c/Users/ysnow/OneDrive/Desktop/db_capstone/H264_Codec/videoTest

# Find the newly uploaded video files
find "$VIDEO_DIR" -type f -name "*.mp4" | while read -r video_file; do
    echo "Processing video: $video_file"
    
    # Extract the filename without the extension
    filename=$(basename -- "$video_file")
    filename_without_extension="${filename%.*}"

    # Run the Docker container, mount the input and output directories
    docker run -v "$VIDEO_DIR:/app/upload" -v "$OUTPUT_DIR:/app/output/$filename_without_extension" "$DOCKER_IMAGE"

    # Extract files created within the Docker container
    docker ps -l -q --filter ancestor="$DOCKER_IMAGE" | while read -r container_id; do
        docker cp "$container_id:/videos" "$OUTPUT_DIR/$filename_without_extension"
    done

    # Remove the input file
    rm "$video_file"
done

#  with timestamp:
# # Create a timestamp with the format YYYYMMDD_HHMMSS
# timestamp=$(date +"%Y%m%d_%H%M%S")

# # Run the Docker container, mount the input and output directories
# docker run -v "$VIDEO_DIR:/app/upload" -v "$OUTPUT_DIR:/app/output/$filename_without_extension-$timestamp" "$DOCKER_IMAGE"

# # Extract files created within the Docker container
# docker ps -l -q --filter ancestor="$DOCKER_IMAGE" | while read -r container_id; do
#     docker cp "$container_id:/path/within/container" "$OUTPUT_DIR/$filename_without_extension-$timestamp"
# done


# shell command:
# inotifywait -m -e create -e moved_to /home/lsnow/upload | while read -r directory event file; do     if [ "$event" = "CREATE" ] || [ "$event" = "MOVED_TO" ]; then         echo "Processing video: $file";         ./process_video.sh "$file";     fi; done
# cp input.mp4 /home/lsnow/upload/