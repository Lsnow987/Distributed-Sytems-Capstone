#!/bin/bash

# This script monitors the /app directory for new or moved-in files
# and uploads each file to AWS S3 using upload_script.py

inotifywait -m /app -e create -e moved_to |
    while read path action file; do
        echo "The file '$file' appeared in directory '$path' via '$action'"
        # Ignore the Zone.Identifier files
        if [[ $file != *":Zone.Identifier" ]]; then
            # Call Python script to upload the file to S3
            ./upload_script.py "${path}${file}"
        fi
    done
