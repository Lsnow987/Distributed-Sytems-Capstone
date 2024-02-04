#!/bin/bash

inotifywait -m -e create -e moved_to /home/lsnow/upload | while read -r directory event file; do
    if [ "$event" = "CREATE" ] || [ "$event" = "MOVED_TO" ]; then
        echo "Processing video: $file"
        ./process_video.sh "$file"
    fi
done