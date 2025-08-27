package com.keshan.cloudage.org.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProfileInfoReq {
    @Size(max = 50 , min = 5)
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
    private LocalDate dateOfBirth;
}
