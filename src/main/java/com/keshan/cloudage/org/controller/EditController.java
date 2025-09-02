package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.common.CustomException;
import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.service.EditService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/edit")
@RequiredArgsConstructor
public class EditController {

    private final EditService editService;
    private final static Set<String> ALLOWED_LIST = Stream.concat(Arrays.stream(ITYPE.values()).map(ITYPE::getFormat),
            Arrays.stream(ITYPE.values()).map(ITYPE::getMIME)). collect(Collectors.toSet());

//TODO later maybe try to use dto for this controller (most likely for request only )
//TODO save the edited image to the s3 and in the DB also try to get these(Controller logics) logics under the edit service
    @GetMapping("formatChange")
    public ResponseEntity<?> formatChange(
            @RequestParam String s3key,
            @NotBlank
            @RequestParam String format
    ) throws IOException {
            checkType(format);
            format = ITYPE.fromFormat(format.toLowerCase()).getFormat();

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
                checkType(MIME);
                return ResponseEntity.ok(editService.imageConversionToASCII(imageBytes,MIME));
            }

    }

    private  void checkType(String type){
        if(!ALLOWED_LIST.contains(type.strip().toLowerCase())){
            throw new CustomException(CustomExceptionCode.INVALID_MIME_TYPE,type);
        }
    }

}
