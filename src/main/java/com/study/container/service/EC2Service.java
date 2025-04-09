package com.study.container.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;

@Service
@RequiredArgsConstructor
public class EC2Service {

    private final Ec2Client ec2Client;

    public void listarInstancias() {
        DescribeInstancesResponse response = ec2Client.describeInstances();
        response.reservations().forEach(reservation -> {
            for (Instance instance : reservation.instances()) {
                System.out.println("ID da Instância: " + instance.instanceId());
            }
        });
    }
}