package com.keshan.cloudage.org.repository;

import com.keshan.cloudage.org.model.Image;
import com.keshan.cloudage.org.model.STATUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,String> {


    Optional<Image> findByS3Key(String objectKey);

    List<Image> findAllByStatus(STATUS status);
}
