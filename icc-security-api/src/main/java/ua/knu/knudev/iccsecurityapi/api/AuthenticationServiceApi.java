package ua.knu.knudev.iccsecurityapi.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.knu.knudev.iccsecurityapi.request.AuthenticationRequest;
import ua.knu.knudev.iccsecurityapi.response.AuthenticationResponse;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public interface AuthenticationServiceApi {

    AuthenticationResponse authenticate(AuthenticationRequest authReq) throws LoginException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
