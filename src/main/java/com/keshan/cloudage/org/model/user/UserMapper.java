package com.keshan.cloudage.org.model.user;


import com.keshan.cloudage.org.auth.request.RegistrationRequest;
import com.keshan.cloudage.org.dto.UpdateProfileInfoReq;
import com.keshan.cloudage.org.dto.UserResponse;
import io.micrometer.common.util.StringUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public void mergeUser(final User savedUser, final UpdateProfileInfoReq req) {
        if (StringUtils.isNotBlank(req.getFullName())
                && !savedUser.getFullName().equals(req.getFullName())
        ){
            savedUser.setFullName(req.getFullName());
        }

        if ( req.getDateOfBirth() != null
                && (savedUser.getDateOfBirth() == null || !savedUser.getDateOfBirth().isEqual(req.getDateOfBirth()))
        ){
            savedUser.setDateOfBirth(req.getDateOfBirth());
        }
    }

    public User toUser(RegistrationRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .accountEnabled(true)
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }

    public UserResponse toUserRes(User user) {
        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : null)
                .createdAt(user.getCreatedDate().toLocalDate())
                .activeAccount(user.isEnabled())
                .build();
    }
}
