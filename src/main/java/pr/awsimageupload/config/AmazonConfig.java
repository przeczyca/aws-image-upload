package pr.awsimageupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    
    @Bean
    public AmazonS3 S3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAUXVEKMXZZC73VXQL", "85juf7zj4UQi626UZI8621ksvpHAzdl5G0cXkVu8");

        return AmazonS3Client.builder().withRegion("us-east-2").withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
}