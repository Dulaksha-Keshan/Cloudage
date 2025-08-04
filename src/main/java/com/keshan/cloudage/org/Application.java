package com.keshan.cloudage.org;

import com.keshan.cloudage.org.util.AWSConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

//		AWSConfig awsConfig = new AWSConfig();
//		System.out.println("Bucket: " + awsConfig.getS3().getBucket());
		SpringApplication.run(Application.class, args);
	}

}
