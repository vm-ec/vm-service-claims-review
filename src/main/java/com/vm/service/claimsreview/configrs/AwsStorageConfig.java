package com.vm.service.claimsreview.configrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.timestreamquery.TimestreamQueryClient;
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient;

@Configuration
public class AwsStorageConfig {

    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    @ConditionalOnProperty(name = "metrics.storage.type", havingValue = "timestream")
    public TimestreamWriteClient timestreamWriteClient() {
        return TimestreamWriteClient.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "metrics.storage.type", havingValue = "timestream")
    public TimestreamQueryClient timestreamQueryClient() {
        return TimestreamQueryClient.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "metrics.storage.type", havingValue = "s3")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
