#!/usr/bin/env python3

import boto3
import sys
import os

def upload_to_s3(file_path, bucket_name):
    # Get the file name from the file path
    file_name = os.path.basename(file_path)
    
    # Specify the region of your S3 bucket
    s3 = boto3.client('s3', region_name='us-east-2')
    try:
        # The third parameter is now just the file_name, not the file_path
        s3.upload_file(file_path, bucket_name, file_name)
        print(f"Successfully uploaded {file_name} to {bucket_name}.")
    except Exception as e:
        print(f"Error uploading {file_name}: {e}")

if __name__ == "__main__":
    file_path = sys.argv[1]  # Get the file path from command line argument
    bucket_name = 'testing1-yonatan-reiter'  # Your bucket name here
    upload_to_s3(file_path, bucket_name)
