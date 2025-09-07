package com.keshan.cloudage.org.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactivationRequest {
    @NotBlank( message = "The email cannot be empty")
    private String email;
}
