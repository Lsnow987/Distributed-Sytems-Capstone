#!/bin/bash

inotifywait -m /app -e create -e moved_to |
    while read path action file; do
        echo "The file '$file' appeared in directory '$path' via '$action'"
        if [[ $file != *":Zone.Identifier" ]]; then
            java -jar /deployments/s3uploader-1.0-SNAPSHOT.jar "${path}${file}"
        fi
    done
