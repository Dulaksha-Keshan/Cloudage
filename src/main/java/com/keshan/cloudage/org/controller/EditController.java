package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.common.CustomException;
import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.service.EditService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/edit")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class EditController {

    private final EditService editService;

//TODO later maybe try to use dto for this controller (most likely for request only )
//TODO save the edited image to the s3 and in the DB
    @GetMapping("formatChange")
    public ResponseEntity<?> formatChange(
            @RequestParam String s3key,
            @RequestParam String format
    ) throws IOException {

        List<String> allowedTypes = Arrays.stream(ITYPE.values()).map(ITYPE::getFormat).toList();
        if (!allowedTypes.contains(format.toLowerCase())) {
            return ResponseEntity.badRequest().body("Invalid format type");
        }else{
            format = ITYPE.fromFormat(format.toLowerCase()).getFormat();
        }
            InputStream image = editService.retrieveImageAsInputStream(s3key);

            byte[] imageBytes = editService.imageConversionToFormat(image,format);
            return ResponseEntity.ok().contentType(MediaType.valueOf(ITYPE.fromFormat(format).getMIME())).body(imageBytes);



    }

    @GetMapping("formatChange/ascii")
    public ResponseEntity<String> formatChangeASCII(
            @RequestParam String s3key,
            @RequestParam(required = false) String MIME
    ) throws IOException {

            byte[] imageBytes = editService.retrieveImageAsBytes(s3key);
            if(MIME == null){
                return ResponseEntity.ok(editService.imageConversionToASCII(imageBytes));

            }else {

                List<String> allowedTypes = Arrays.stream(ITYPE.values()).map(ITYPE::getMIME).toList();
                if (!typeValid(allowedTypes,MIME)) {
                    throw new CustomException(CustomExceptionCode.INVALID_MIME_TYPE,MIME);
                }

                return ResponseEntity.ok(editService.imageConversionToASCII(imageBytes,MIME));
            }

    }

    private boolean typeValid(List<String> allowedList,String type){

        return allowedList.contains(type.strip().toLowerCase());
    }

}
