package com.keshan.cloudage.org.auth.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshRequest {
    @NotBlank(message = "Refresh token is empty")
    private String refreshToken;
}
