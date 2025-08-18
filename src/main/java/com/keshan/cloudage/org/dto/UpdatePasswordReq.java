package com.keshan.cloudage.org.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePasswordReq {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
