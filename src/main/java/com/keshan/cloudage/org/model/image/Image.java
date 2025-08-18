package com.keshan.cloudage.org.model.image;


import com.keshan.cloudage.org.model.enums.STATUS;
import com.keshan.cloudage.org.model.user.User;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}