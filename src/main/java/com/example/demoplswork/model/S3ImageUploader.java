package com.example.demoplswork.model;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Path;
import java.nio.file.Paths;

public class S3ImageUploader {
    private static final String ACCESS_KEY = "AKIAS6J7QQO5IK6YLW5W";
    private static final String SECRET_KEY = "esXAH4uGCqKuD863J4fQfqpmeCpTJ4YBwFRrVyqK";
    private static final String BUCKET_NAME = "hobby-log"; // Replace with your bucket name
    private final S3Client s3Client;

    public S3ImageUploader() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_2)  // Set your bucket region
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }


    public String uploadImage(String filePath, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        try {
            s3Client.putObject(putObjectRequest, Paths.get(filePath));
            System.out.println("File uploaded successfully.");

            // Generate the URL for the uploaded file
            String s3Url = "https://" + BUCKET_NAME + ".s3.ap-southeast-2" + ".amazonaws.com/" + fileName;
            return s3Url;

        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            return null;  // Return null if there was an error
        }
    }

}
