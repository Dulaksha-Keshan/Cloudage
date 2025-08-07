package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.model.Image;
import com.keshan.cloudage.org.service.S3DownloadService;
import com.keshan.cloudage.org.service.S3UploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("api/s3")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class S3Controller {

    private final S3UploadService s3UploadService;
    private final S3DownloadService s3DownloadService;

    @GetMapping("upload-req/{fileName}")
    public ResponseEntity<String> getUploadUrl(
            @PathVariable("fileName") String fileName
            ){

         String objectKey = "images/"+ fileName + UUID.randomUUID();
         URL uploadUrl = s3UploadService.generatePutObjectUrl(objectKey,fileName);


         return ResponseEntity.ok(uploadUrl.toString());
    }

//    @PostMapping("upload-confirm")
//    public ResponseEntity<Optional<Image>> confirmUpload(){
//
//    }

    @GetMapping("get-images")//TODO add path variable later
    public ResponseEntity<List<String>> userS3Keys(
            //TODO the path variable for the user id will go here later when users are create
    ){
        try{
            List<String> linkList = s3DownloadService.userImagesLinkList();
            return ResponseEntity.ok(linkList);
        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}
