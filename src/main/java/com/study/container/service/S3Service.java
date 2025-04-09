package com.study.container.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Response;

import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public void criarBucket(String bucketName) {
        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(bucketRequest);
            System.out.println("Bucket criado: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Erro ao criar bucket: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void uploadArquivo(String bucketName, String key, String filePath) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.putObject(objectRequest, Paths.get(filePath));
            System.out.println("Arquivo enviado: " + key);
        } catch (S3Exception e) {
            System.err.println("Erro ao enviar arquivo: " + e.awsErrorDetails().errorMessage());
        }
    }
}