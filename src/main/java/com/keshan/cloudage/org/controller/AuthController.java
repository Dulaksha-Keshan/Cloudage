package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.auth.AuthenticationService;
import com.keshan.cloudage.org.auth.request.AuthenticationRequest;
import com.keshan.cloudage.org.auth.request.ReactivationRequest;
import com.keshan.cloudage.org.auth.request.RefreshRequest;
import com.keshan.cloudage.org.auth.request.RegistrationRequest;
import com.keshan.cloudage.org.auth.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>login(
            @Valid
            @RequestBody
            final AuthenticationRequest  req,
            HttpServletResponse res
            ){

        AuthenticationResponse authRes = this.authenticationService.login(req,res);

        return ResponseEntity.ok(authRes);

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
            @CookieValue(name = "refreshToken" )
            final String refreshToken
    ){
        @Valid
        final RefreshRequest req = RefreshRequest.builder()
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(this.authenticationService.refreshToken(req));
    }

    @PatchMapping("/reactivate")
    @ResponseStatus(HttpStatus.OK)
    public void reactivateAccount(
           final @RequestParam String token
    ){
        this.authenticationService.accountReactivation(token);
    }


    @PostMapping("/reactivation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivationRequest(
            @RequestBody
            @Valid
            final ReactivationRequest req
    ){

        this.authenticationService.accountReactivationRequest(req);
    }

}
