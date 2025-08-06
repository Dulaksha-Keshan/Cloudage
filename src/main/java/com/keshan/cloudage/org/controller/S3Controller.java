package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.service.S3UploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.UUID;

@RestController()
@RequestMapping("api/s3")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class S3Controller {

    private S3UploadService s3UploadService;

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
}
