package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.model.image.Image;
import com.keshan.cloudage.org.model.enums.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

//    //TODO have make this get and user id and return only that users images,then make sure to create a method in repo to get by user id and Optional
//    public List<String> userImagesLinkList(){
//
//        logger.info("Fetching user Images Links........");
//
//        return Optional.ofNullable(imageRepository.findAllByStatus(STATUS.COMPLETED))
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(image -> "https://" + bucket + ".s3.amazonaws.com/" + image.getS3Key())
//                .toList();
//
//
//    }


    public Map<String ,String> userImageList (String userId){
        logger.info("Fetching user Images Links........");

//        List<Image> images = Optional.ofNullable(imageRepository.findAllByStatus(STATUS.COMPLETED))
//                .orElse(Collections.emptyList());
//
//        return  images.stream().collect(Collectors.toMap((UUID.randomUUID().toString() + Image::getOriginalFileName) , Image::getS3Key));
        return Optional.ofNullable(imageRepository.findAllByStatusAndUserId(STATUS.COMPLETED,userId))
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors
                        .toMap(Image::getS3Key, image -> "https://" + bucket + ".s3.amazonaws.com/" + image.getS3Key()));





    }

    public String downloadLink (String s3Key){
        return  "https://" + bucket + ".s3.amazonaws.com/" + s3Key;
    }

}
