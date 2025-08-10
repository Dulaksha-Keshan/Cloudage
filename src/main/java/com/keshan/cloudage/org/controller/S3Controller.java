package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.model.ITYPE;
import com.keshan.cloudage.org.service.S3DownloadService;
import com.keshan.cloudage.org.service.S3UploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
            @PathVariable("fileName") String fileName,
            @RequestParam String type
    ) {

        List<String> allowedTypes = Arrays.stream(ITYPE.values()).map(ITYPE::getMIME).toList();

        if (!allowedTypes.contains(type)) {
            return ResponseEntity.badRequest().body("Invalid file type");
        }

        String objectKey = "images/" + UUID.randomUUID()+"_" + fileName;
        URL uploadUrl = s3UploadService.generatePutObjectUrl(objectKey, fileName,type);


        return ResponseEntity.ok(uploadUrl.toString());
    }

//    @PostMapping("upload-confirm")
//    public ResponseEntity<Optional<Image>> confirmUpload(){
//
//    }

    @GetMapping("get-images")//TODO add path variable later
    public ResponseEntity<Map<String, String>> userS3Keys(
            //TODO the path variable for the user id will go here later when users are create
    ) {
        try {
            Map<String, String> linkList = s3DownloadService.userImageList();
            return ResponseEntity.ok(linkList);
        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }


//    @GetMapping("u")
}
