package com.keshan.cloudage.org.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.model.image.Image;
import com.keshan.cloudage.org.model.enums.STATUS;
import com.keshan.cloudage.org.model.user.User;
import com.keshan.cloudage.org.repository.ImageRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
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
    private final ObjectMapper objectMapper;


    public S3UploadService(
            S3Client s3Client,

            S3Presigner presigner,

            ImageRepository imageRepository,

            @Value("${aws-settings.s3-bucket}") String bucket ,

            ObjectMapper objectMapper
    ) {
        this.s3Client = s3Client;
        this.presigner = presigner;
        this.imageRepository = imageRepository;
        this.bucket = bucket;
        this.objectMapper = objectMapper;

    }

    public URL  generatePutObjectUrl (String objectKey , String fileName, String type, int size, User user){

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(type)
                .build();


        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(20))
                .putObjectRequest(putObjectRequest)
                .build();

        if (objectKey != null) {
            Image image = new Image();
            image.setStatus(STATUS.PENDING);
            image.setS3Key(objectKey);
            image.setOriginalFileName(fileName);
            image.setSize((size/1000));
            image.setOriginalFormat(ITYPE.fromMIME(type).getFormat());
            image.setUser(user);
            imageRepository.save(image);
        }

        return presigner.presignPutObject(putObjectPresignRequest).url();

    }



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

    @SqsListener("image-upload-queue")
    public void updateUploadStatus(String msg) throws JsonProcessingException {

        logger.info("Sqs Message : " + msg);


//        try {
//            JsonNode root = this.objectMapper.readTree(msg);
//
//            if (root.has("Event") && "s3:TestEvent".equals(root.get("Event").asText())) {
//                logger.info("Skipping S3 test event");
//                return;
//            }
//
//            if(root != null){
//                for(JsonNode record : root.get("Records")){
//
//                    String eventName = record.get("eventName").asText();
//                    if (!eventName.startsWith("ObjectCreated:")) {
//                        logger.info("Skipping non-upload event: " + eventName);
//                        continue;
//                    }
//
//                    String key = record.get("s3").get("object").get("key").asText();
//
//                    imageRepository.findByS3Key(key).ifPresent(image -> {
//                        image.setStatus(STATUS.COMPLETED);
//                        imageRepository.save(image);
//                        logger.info("Upload confirmed for key :" + key);
//                    });
//            }
//
//            }
        try {
            JsonNode root = this.objectMapper.readTree(msg);

            // Skip test event
            if (root.has("Event") && "s3:TestEvent".equals(root.get("Event").asText())) {
                logger.info("Skipping S3 test event");
                return;
            }

            // Ensure "Records" exists and is an array
            if (!root.has("Records") || !root.get("Records").isArray()) {
                logger.info("Skipping message without Records array");
                return;
            }

            for (JsonNode record : root.get("Records")) {
                String eventName = record.get("eventName").asText();
                if (!eventName.startsWith("ObjectCreated:")) {
                    logger.info("Skipping non-upload event: " + eventName);
                    continue;
                }

                String key = record.get("s3").get("object").get("key").asText();

                imageRepository.findByS3Key(key).ifPresent(image -> {
                    image.setStatus(STATUS.COMPLETED);
                    imageRepository.save(image);
                    logger.info("Upload confirmed for key: " + key);
                });
            }
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
