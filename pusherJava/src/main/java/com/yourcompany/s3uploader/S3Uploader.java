package com.yourcompany.s3uploader;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

public class S3Uploader {

    public static void main(String[] args) {
        String filePath = args[0];
        String bucketName = "testing1-yonatan-reiter"; // Your bucket name here

        // Get the file name from the file path
        String fileName = Paths.get(filePath).getFileName().toString();

        // Create an S3 client
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAW3MD7UEOPMNOYO7C", "RD1oQrnRpfeccd1jY2LGeXuVequuqK+KIYcxAHWo");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                .withRegion(Regions.US_EAST_2)
                                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                                .build();

        try {
            // Upload the file
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, new File(filePath)));
            System.out.println("Successfully uploaded " + fileName + " to " + bucketName);
        } catch (AmazonServiceException e) {
            System.err.println("Error uploading " + fileName + ": " + e.toString());
        }
    }
}
