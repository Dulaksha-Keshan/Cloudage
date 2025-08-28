package com.keshan.cloudage.org.auth.request;


import com.keshan.cloudage.org.common.NonDisposableEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    @Size(max = 50 , min = 5)
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
    @NotBlank(message = "Email name cannot be empty")
    @Email
    @Size(max = 50 , min = 5)
    @NonDisposableEmail
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(max = 50 , min = 6)
    private String password;
    @NotBlank(message = "Password confirm cannot be empty")
    @Size(max = 50 , min = 5)
    private String confirmPassword;

}
