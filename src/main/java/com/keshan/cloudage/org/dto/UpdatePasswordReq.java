package com.keshan.cloudage.org.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePasswordReq {
    @NotBlank(message = "Current password cannot be empty")
    @Size(max = 50 , min = 6)
    private String currentPassword;
    @NotBlank(message = "New password cannot be empty")
    @Size(max = 50 , min = 6)
    private String newPassword;
    @NotBlank(message = "Confirm password cannot be empty")
    @Size(max = 50 , min = 6)
    private String confirmPassword;
}
