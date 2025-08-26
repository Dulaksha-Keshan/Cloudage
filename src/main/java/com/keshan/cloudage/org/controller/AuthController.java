package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.auth.AuthenticationService;
import com.keshan.cloudage.org.auth.request.AuthenticationRequest;
import com.keshan.cloudage.org.auth.request.RefreshRequest;
import com.keshan.cloudage.org.auth.request.RegistrationRequest;
import com.keshan.cloudage.org.auth.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>login(
            @Valid
            @RequestBody
            final AuthenticationRequest  req
            ){

        return ResponseEntity.ok(this.authenticationService.login(req));

    }

    @PostMapping("/register")
    public ResponseEntity<Void>register(
            @Valid
            @RequestBody
            final RegistrationRequest req
    ){

        this.authenticationService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse>refresh(
            @Valid
            @RequestBody
            final RefreshRequest req
    ){
        return ResponseEntity.ok(this.authenticationService.refreshToken(req));
    }

}
