package com.keshan.cloudage.org.auth;

import com.keshan.cloudage.org.auth.request.AuthenticationRequest;
import com.keshan.cloudage.org.auth.request.RefreshRequest;
import com.keshan.cloudage.org.auth.request.RegistrationRequest;
import com.keshan.cloudage.org.auth.response.AuthenticationResponse;
import com.keshan.cloudage.org.common.CustomException;
import com.keshan.cloudage.org.jwt.JwtService;
import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import com.keshan.cloudage.org.model.user.User;
import com.keshan.cloudage.org.model.user.UserMapper;
import com.keshan.cloudage.org.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {

        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final User user = (User) auth.getPrincipal();
        final String token = this.jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
        final String tokenType = "Bearer ";

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();
    }

    @Override
    public void register(RegistrationRequest request) {

        checkEmail(request.getEmail());
        checkPassword(request.getPassword() , request.getConfirmPassword());


       final User user = this.userMapper.toUser(request);
       log.info("Saving user {}",user);

       this.userRepository.save(user);

    }


    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) {

        final String newAccessToken = this.jwtService.refreshAccessToken(request.getRefreshToken());
        final String tokenType = "Bearer ";

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType(tokenType)
                .build();
    }


    private void checkPassword(String password, String confirmPassword) {

        if(password == null || !password.equals(confirmPassword)){
            throw  new CustomException(CustomExceptionCode.PASSWORD_MISMATCH);
        }

    }

    private void checkEmail(String email) {
        final boolean emailExists = this.userRepository.existByEmailIgnoreCase(email);
        if(emailExists){
            throw new CustomException(CustomExceptionCode.EMAIL_ALREADY_EXITS);
        }
    }

}
