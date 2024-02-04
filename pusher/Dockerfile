FROM python:3.8-slim

# Install inotify-tools and AWS CLI
RUN apt-get update && apt-get install -y inotify-tools awscli

# Install Boto3 for Python script
RUN pip install boto3

RUN apt-get install -y dos2unix

# Set the working directory in the container
WORKDIR /app

# Copy the scripts into the container
COPY inotify_script.sh ./inotify_script.sh
COPY upload_script.py ./upload_script.py

# Make the inotify script executable
RUN chmod +x inotify_script.sh

# Command to run when the container starts
CMD ["./inotify_script.sh"]
