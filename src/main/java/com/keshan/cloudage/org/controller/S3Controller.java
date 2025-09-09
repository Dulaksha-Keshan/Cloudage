package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.model.user.User;
import com.keshan.cloudage.org.service.S3Service;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/upload-req/{fileName}")
    public ResponseEntity<String> getUploadUrl(
            final Authentication principal,
            @PathVariable("fileName") String fileName,
            @RequestParam String type,
            @RequestParam int size
    ) {

        List<String> allowedTypes = Arrays.stream(ITYPE.values()).map(ITYPE::getMIME).toList();

        if (!allowedTypes.contains(type)) {
            return ResponseEntity.badRequest().body("Invalid file type");
        }
        String objectKey = "images/" + UUID.randomUUID()+"_" + fileName;
        URL uploadUrl = s3Service.generatePutObjectUrl(objectKey, fileName,type,size,(User)principal.getPrincipal());
        return ResponseEntity.ok(uploadUrl.toString());
    }


    @GetMapping("/get-images")
    public ResponseEntity<Map<String, String>> userS3Keys(
           final Authentication principal
    ) {
            Map<String, String> linkList = s3Service.userImageList( getUserId(principal));
            return ResponseEntity.ok(linkList);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteS3Image(
            final Authentication principal,
            @RequestParam
            @NotBlank
            final String s3key
    ){

        this.s3Service.deleteImage(getUserId(principal),s3key);

    }


    private String getUserId(
            final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }


}
