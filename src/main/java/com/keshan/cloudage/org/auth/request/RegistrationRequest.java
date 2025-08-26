package com.keshan.cloudage.org.auth.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
//TODO validations
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;

}
