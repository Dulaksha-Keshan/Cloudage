package com.keshan.cloudage.org.auth;

import com.keshan.cloudage.org.auth.request.AuthenticationRequest;
import com.keshan.cloudage.org.auth.request.ReactivationRequest;
import com.keshan.cloudage.org.auth.request.RefreshRequest;
import com.keshan.cloudage.org.auth.request.RegistrationRequest;
import com.keshan.cloudage.org.auth.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request , HttpServletResponse response);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);


    void accountReactivation(String email);


    void accountReactivationRequest(ReactivationRequest req);
}
