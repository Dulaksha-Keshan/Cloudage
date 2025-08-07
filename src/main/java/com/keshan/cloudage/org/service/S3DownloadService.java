package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.model.Image;
import com.keshan.cloudage.org.model.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service

public class S3DownloadService {

    private final ImageRepository imageRepository;
    private final Logger logger = Logger.getLogger(S3DownloadService.class.getName());
    private final String bucket;

    public S3DownloadService(
            ImageRepository imageRepository, @Value("${aws-settings.s3-bucket}") String bucket
    ) {
        this.imageRepository = imageRepository;
        this.bucket = bucket;
        logger.info("Target S3 Bucket: " + this.bucket);
    }

    //TODO have make this get and user id and return only that users images,then make sure to create a method in repo to get by user id and Optional
    public List<String> userImagesLinkList(){

        logger.info("Fetching user Images Links........");

        return Optional.ofNullable(imageRepository.findAllByStatus(STATUS.COMPLETED))
                .orElse(Collections.emptyList())
                .stream()
                .map(image -> "https://" + bucket + ".s3.amazonaws.com/" + image.getS3Key())
                .toList();


    }

}
