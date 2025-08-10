package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.model.ITYPE;
import com.keshan.cloudage.org.service.EditService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/edit")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class EditController {

    private final EditService editService;


    @GetMapping("formatChange")
    public ResponseEntity<?> formatChange(
            @RequestParam String s3key,
            @RequestParam String format
    ) throws IOException {

        List<String> allowedTypes = Arrays.stream(ITYPE.values()).map(ITYPE::getFormat).toList();
        if (!allowedTypes.contains(format.toLowerCase())) {
            return ResponseEntity.badRequest().body("Invalid format type");
        }

        try {
            BufferedImage image = editService.retrieveImage(s3key);

            byte[] imageBytes = editService.imageConversion(image,format);
            return ResponseEntity.ok().contentType(MediaType.valueOf(ITYPE.fromFormat(format).getMIME())).body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }

}
