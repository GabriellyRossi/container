package com.study.container.controller;

import com.study.container.service.EC2Service;
import com.study.container.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AwsController {

    private final S3Service s3Service;
    private final EC2Service ec2Service;

    // Endpoint para criar um bucket S3
    @PostMapping("/s3/buckets/{bucketName}")
    public ResponseEntity<Void> criarBucket(@PathVariable String bucketName) {
        s3Service.criarBucket(bucketName);
        return ResponseEntity.ok().build();
    }

    // Endpoint para fazer upload de um arquivo para o S3
    @PostMapping("/s3/upload")
    public ResponseEntity<Void> uploadArquivo(@RequestParam String bucketName,
                                              @RequestParam String key,
                                              @RequestParam String filePath) {
        s3Service.uploadArquivo(bucketName, key, filePath);
        return ResponseEntity.ok().build();
    }

    // Endpoint para listar instâncias EC2
    @GetMapping("/ec2/instances")
    public ResponseEntity<Void> listarInstancias() {
        ec2Service.listarInstancias();
        return ResponseEntity.ok().build();
    }
}