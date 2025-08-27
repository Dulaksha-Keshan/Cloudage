package com.keshan.cloudage.org.auth.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    @NotBlank(message = "Email cannot be  empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(max = 50,min =6)
    private String password;

}
