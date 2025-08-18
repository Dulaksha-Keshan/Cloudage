package com.keshan.cloudage.org.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProfileInfoReq {

    private String fullName;
    private LocalDate dateOfBirth;
}
