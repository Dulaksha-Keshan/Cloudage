package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.model.Image;
import com.keshan.cloudage.org.model.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import com.keshan.cloudage.org.util.AWSConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.logging.Logger;

@Service
public class S3UploadService {

    private final S3Presigner presigner;
    private final String bucket;
    private final ImageRepository imageRepository;
    private final S3Client s3Client;
    private final Logger logger = Logger.getLogger(S3UploadService.class.getName());
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
        this.s3Client = S3Client.builder().region(Region.of(awsConfig.getRegion().getStatic_())).build();
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

        if (objectKey != null) {
            Image image = new Image();
            image.setStatus(STATUS.PENDING);
            image.setS3Key(objectKey);
            image.setOriginalFileName(fileName);
            imageRepository.save(image);
        }

        return presigner.presignPutObject(putObjectPresignRequest).url();

    }

    public boolean updateUploadStatus(String objectKey){

        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        try {
            s3Client.headObject(request);
            return imageRepository.findByS3Key(objectKey).map(image -> {
                image.setStatus(STATUS.COMPLETED);
                imageRepository.save(image);
                return true;
            }).orElse(false);

        }catch (S3Exception exception){
            logger.warning("S3 object not found or error: " + exception.awsErrorDetails().errorMessage());
            return false;
        }

    }// this is for the upload checker on the same upload controller frontend has to send a request to upload after success
    //have to figure out how to pass the object since the object key is not being passed to the front end maybe use a json with few key values for the url,s3Key



}
