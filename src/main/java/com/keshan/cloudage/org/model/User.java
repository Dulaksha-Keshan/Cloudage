package com.keshan.cloudage.org.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fullName;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    orphanRemoval = true)
    private List<Image> images;


}
