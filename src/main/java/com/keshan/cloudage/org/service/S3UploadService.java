package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.model.image.Image;
import com.keshan.cloudage.org.model.enums.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
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
            S3Client s3Client,

            S3Presigner presigner,

            ImageRepository imageRepository,

            @Value("${aws-settings.s3-bucket}") String bucket
    ) {
        this.s3Client = s3Client;
        this.presigner = presigner;
        this.imageRepository = imageRepository;
        this.bucket = bucket;

        logger.info("S3UploadService initialized successfully with auto-configured clients.");
        logger.info("Target S3 Bucket: " + this.bucket);
    }

    public URL  generatePutObjectUrl (String objectKey , String fileName, String type,int size){

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(type)
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
            image.setSize((size/1000));
            image.setOriginalFormat(ITYPE.fromMIME(type).getFormat());
            imageRepository.save(image);
        }

        return presigner.presignPutObject(putObjectPresignRequest).url();

    }

//    public boolean updateUploadStatus(String objectKey){
//
//        HeadObjectRequest request = HeadObjectRequest.builder()
//                .bucket(bucket)
//                .key(objectKey)
//                .build();
//
//        try {
//            s3Client.headObject(request);
//            return imageRepository.findByS3Key(objectKey).map(image -> {
//                image.setStatus(STATUS.COMPLETED);
//                imageRepository.save(image);
//                return true;
//            }).orElse(false);
//
//        }catch (S3Exception exception){
//            logger.warning("S3 object not found or error: " + exception.awsErrorDetails().errorMessage());
//            return false;
//        }
//
//    }// this is for the upload checker on the same upload controller frontend has to send a request to upload after success
//    //have to figure out how to pass the object since the object key is not being passed to the front end maybe use a json with few key values for the url,s3Key



    public void updateUploadStatusAll(){

        try{
            imageRepository.findAllByStatus(STATUS.PENDING).parallelStream().forEach(
                    image -> {
                        HeadObjectRequest request = HeadObjectRequest.builder()
                                .bucket(bucket)
                                .key(image.getS3Key())
                                .build();
                        try{
                            s3Client.headObject(request);

                            image.setStatus(STATUS.COMPLETED);

                        }catch (NoSuchKeyException ex){
                            image.setStatus(STATUS.FAILED);
                            logger.warning("S3 object not found or error: " + ex.awsErrorDetails().errorMessage());
                        }
                        imageRepository.save(image);
                    }
            );
        }catch (Exception e){
            logger.warning(e.getMessage());
        }


    }

    public void updateUploadStatus(String s3key){

    }

}
