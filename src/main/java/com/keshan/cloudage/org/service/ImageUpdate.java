package com.keshan.cloudage.org.service;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@AllArgsConstructor
//TODO make this as entity listened class
public class ImageUpdate {

    private S3UploadService s3UploadService;

    private  final Logger logger = Logger.getLogger(ImageUpdate.class.getName());

    @Scheduled(fixedRate = 60000)
    public void autoUpdate(){
        try{
            logger.info("Updating image status........");
            s3UploadService.updateUploadStatusAll();
        }catch (Exception e){
            logger.warning(e.getMessage());
        }finally {
            logger.info("Updating completed......");
        }

    }
}
