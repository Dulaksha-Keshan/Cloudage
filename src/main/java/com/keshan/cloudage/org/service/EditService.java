package com.keshan.cloudage.org.service;


import com.keshan.cloudage.org.common.CustomException;
import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import com.keshan.cloudage.org.model.enums.ITYPE;
import com.keshan.cloudage.org.model.enums.STATUS;
import com.keshan.cloudage.org.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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

//        logger.info("EditService  initialized successfully with auto-configured clients.");
//        logger.info("Target S3 Bucket: " + this.bucket);
    }



    public byte[] imageConversionToFormat (InputStream inputStream , String format) throws IOException {

        try {
            if(format.equalsIgnoreCase(ITYPE.GIF.getFormat())){
                return inputStreamToFormat(inputStream,format);
            }else {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                return bufferedImageToFormat(bufferedImage,format);
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte[] bufferedImageToFormat (BufferedImage image , String format) throws IOException {

        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(format).next();

        ImageWriteParam defaultParams = imageWriter.getDefaultWriteParam();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream(image.getHeight()*image.getWidth()*5); ImageOutputStream IOS = ImageIO.createImageOutputStream(baos); ){

            imageWriter.setOutput(IOS);

            imageWriter.write(null,new IIOImage(image,null,null),defaultParams);

            return baos.toByteArray();
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }finally {
            imageWriter.dispose();
        }


    }

    public byte[] inputStreamToFormat(InputStream inputStream , String format) throws IOException{

        ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
        try (ImageInputStream iis = ImageIO.createImageInputStream(inputStream)) {
            reader.setInput(iis, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(format).next();

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                imageWriter.setOutput(ios);
                imageWriter.prepareWriteSequence(null);

                int numFrames = reader.getNumImages(true);
                for (int i = 0; i < numFrames; i++) {
                    BufferedImage frame = reader.read(i);
                    IIOMetadata metadata = reader.getImageMetadata(i);
                    imageWriter.writeToSequence(new IIOImage(frame, null, metadata), imageWriter.getDefaultWriteParam());
                }

                imageWriter.endWriteSequence();



            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }finally {
                imageWriter.dispose();

            }
            return baos.toByteArray();
        } finally {
            reader.dispose();
        }

    }

    public String imageConversionToASCII (byte[] imageBytes ) throws IOException {

        return Base64.getEncoder().encodeToString(imageBytes);

    }

    // when you directly want to use the string base image on the front end
    public String imageConversionToASCII (byte[] imageBytes , String MIME ) throws IOException {

        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return String.format("data:%s;base64,%s",MIME,encodedImage);


    }



    public BufferedImage retrieveImageAsBufferedImage (String s3Key) throws IOException {

        try {
            String confirmedKey = imageRepository.findByS3KeyAndStatus(s3Key, STATUS.COMPLETED)
                    .orElseThrow(() -> new CustomException(CustomExceptionCode.IMAGE_NOT_FOUND, s3Key))
                    .getS3Key();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(confirmedKey)
                    .build();

            try (ResponseInputStream<GetObjectResponse> s3image = s3Client.getObject(getObjectRequest)) {
                return ImageIO.read(s3image);
            }
        }catch (IOException exception){
            throw new IOException(exception.getMessage());
        }
    }

    public byte[] retrieveImageAsBytes (String s3Key) throws IOException {
        String confirmedKey = imageRepository.findByS3KeyAndStatus(s3Key, STATUS.COMPLETED).
                orElseThrow(() -> new CustomException(CustomExceptionCode.IMAGE_NOT_FOUND, s3Key))
                .getS3Key();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(confirmedKey)
                .build();


        ResponseBytes<GetObjectResponse> s3imageBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return s3imageBytes.asByteArray();

    }

    public InputStream retrieveImageAsInputStream (String s3Key) {
        String confirmedKey = imageRepository.findByS3KeyAndStatus(s3Key, STATUS.COMPLETED)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.IMAGE_NOT_FOUND, s3Key))
                .getS3Key();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(confirmedKey)
                .build();

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());

    }




}
