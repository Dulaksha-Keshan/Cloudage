package com.keshan.cloudage.org.util;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws")
@Data
public class AWSConfig {

     private  Credentials credentials;

     private  Region  region ;

     private S3 s3;


    @Data
     public  static class  Credentials {

        private String accessKey ;

        private String secretKey ;


    }

    @Data
    public static  class Region {

        private String static_ ;

    }

    @Data
    public static  class S3{

        private String bucket;
    }

}
