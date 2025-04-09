package com.study.container.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsConfig {

    private static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";
    private static final String AWS_ACCESS_KEY = "test";
    private static final String AWS_SECRET_KEY = "test";

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)
                ))
                .build();
    }

    @Bean
    public Ec2Client ec2Client() {
        return Ec2Client.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)
                ))
                .build();
    }
}