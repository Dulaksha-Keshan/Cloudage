package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.model.Image;
import com.keshan.cloudage.org.model.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import com.keshan.cloudage.org.util.AWSConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class S3UploadService {

    private final S3Presigner presigner;
    private final String bucket;
    private final ImageRepository imageRepository;

    public S3UploadService(
            AWSConfig awsConfig,
            ImageRepository imageRepository
    ) {

        this.bucket = awsConfig.getS3().getBucket();
        this.presigner = S3Presigner.builder()
                .region(Region.of(awsConfig.getRegion().getStatic_()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        awsConfig.getCredentials().getAccessKey(),
                        awsConfig.getCredentials().getSecretKey()
                )
                        ))
                .build();
        this.imageRepository = imageRepository;
    }

    public URL  generatePutObjectUrl (String objectKey , String fileName){

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();


        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        Image image = new Image();
        image.setStatus(STATUS.PENDING);
        image.setS3Key(objectKey);
        image.setOriginalFileName(fileName);
        imageRepository.save(image);

        return presigner.presignPutObject(putObjectPresignRequest).url();

    }

//    public Image saveImageMetaData(String objectKey, String fileName) {
//
//    }

//

}
