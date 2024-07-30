package kea.enter.enterbe.global.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class ObjectStorageConfig {
    @Value("${kakao.cloud.bucket.endpoint}")
    private String s3Endpoint;
    @Value("${kakao.cloud.access}")
    private String accessKey;
    @Value("${kakao.cloud.secret}")
    private String secretAccessKey;
    @Value("${kakao.cloud.region}")
    private String region;

    @Bean
    public S3Client defaultS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretAccessKey);
        URI uri = URI.create(s3Endpoint);
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(region))
            .endpointOverride(uri)
            .forcePathStyle(true)
            .build();
    }
}