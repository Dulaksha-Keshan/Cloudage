package com.keshan.cloudage.org.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id;

    private String originalFileName;

    private String s3Key ;

    private String originalFormat;

    @Column(name = "size_KB")
    private  int size;

    @Enumerated(EnumType.STRING)
    private STATUS status;


}