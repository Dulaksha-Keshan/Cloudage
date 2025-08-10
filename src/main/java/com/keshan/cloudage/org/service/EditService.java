package com.keshan.cloudage.org.service;


import com.keshan.cloudage.org.model.ITYPE;
import com.keshan.cloudage.org.model.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class EditService {

    private final S3Client s3Client;
    private final Logger logger = Logger.getLogger(EditService.class.getName());
    private final String bucket;
    private final ImageRepository imageRepository;

    public EditService(
            S3Client s3Client,
            @Value("${aws-settings.s3-bucket}")String bucket,
            ImageRepository imageRepository
    ) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.imageRepository = imageRepository;

        logger.info("EditService  initialized successfully with auto-configured clients.");
        logger.info("Target S3 Bucket: " + this.bucket);
    }


//    public byte[] applyFilter(MultipartFile imageFile , String encode) throws IOException {
//
//        BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
//
//
//
//
//
//    }
//
//
//    public String


    public byte[] imageConversion (BufferedImage image , String format) throws IOException {

//        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
//
//        ImageIO.write(image,format,baos);
//        return baos.toByteArray();


        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(format).next();

        ImageWriteParam defaultParams = imageWriter.getDefaultWriteParam();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream(image.getHeight()*image.getWidth()*7)){

            logger.info("Image size :" + (image.getHeight()*image.getWidth()*7));
            ImageOutputStream IOS = ImageIO.createImageOutputStream(baos);

            imageWriter.setOutput(IOS);

            imageWriter.write(null,new IIOImage(image,null,null),defaultParams);

            IOS.flush();
            return baos.toByteArray();
        }catch (IOException e){
            logger.warning(e.getMessage());
            return null;
        }finally {
            imageWriter.dispose();
        }


    }

    public BufferedImage retrieveImage (String s3Key) throws IOException {

        try {
            String confirmedKey = imageRepository.findByS3KeyAndStatus(s3Key, STATUS.COMPLETED).orElseThrow(() -> new IOException("No image found for key: " + s3Key)).getS3Key();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(confirmedKey)
                    .build();

            try (ResponseInputStream<GetObjectResponse> s3image = s3Client.getObject(getObjectRequest)) {
                return ImageIO.read(s3image);
            }
        }catch (IOException exception){
            logger.warning(exception.getMessage());
        }
        return null;
    }


}
