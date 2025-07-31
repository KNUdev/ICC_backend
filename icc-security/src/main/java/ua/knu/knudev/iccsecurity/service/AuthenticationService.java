package ua.knu.knudev.iccsecurity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.iccsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.iccsecurity.exception.TokenException;
import ua.knu.knudev.iccsecurityapi.api.AuthenticationServiceApi;
import ua.knu.knudev.iccsecurityapi.dto.Tokens;
import ua.knu.knudev.iccsecurityapi.request.AuthenticationRequest;
import ua.knu.knudev.iccsecurityapi.response.AuthenticationResponse;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceApi {

    private final EmployeeAuthService employeeAuthService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final String DEFAULT_ADMIN_USERNAME = System.getenv("ADMIN_USERNAME");
    private static final String DEFAULT_ADMIN_PASSWORD = System.getenv("ADMIN_PASSWORD");

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authReq) throws LoginException {
        Optional<AuthenticatedEmployee> account = employeeAuthService.getDomainByEmail(authReq.email());

        try {
            checkAccountValidity(account);
        } catch (LoginException e) {
            if (!authReq.email().equals(DEFAULT_ADMIN_USERNAME)
                    || !authReq.password().equals(DEFAULT_ADMIN_PASSWORD)
                    || employeeAuthService.existsByRole(EmployeeAdministrativeRole.HEAD_MANAGER)) {
                throw new LoginException();
            }

            return authenticateAdmin();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authReq.email(), authReq.password())
            );
        } catch (BadCredentialsException e) {
            throw new LoginException();
        }

        Tokens tokens = jwtService.generateTokens(account.get());
        return AuthenticationResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Authorization header is invalid"
            );
            return;
        }
        String refreshToken = authHeader.substring(7);

        try {
            if (jwtService.isAccessToken(refreshToken)) {
                throw new TokenException("Please enter refresh token", HttpStatus.BAD_REQUEST);
            }
        } catch (ExpiredJwtException ex) {
            throw new TokenException("Your token is expired. Please re-authenticate", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException | MalformedJwtException ex) {
            throw new TokenException("Your token is invalid", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtService.extractEmail(refreshToken);
        if (email != null) {
            Optional<AuthenticatedEmployee> account = employeeAuthService.getDomainByEmail(email);
            if (account.isEmpty()) {
                throw new TokenException(
                        "The account associated with the provided token cannot be found. " +
                                "Please go to /authenticate to obtain a new token via authentication.",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (jwtService.isAccessTokenValid(refreshToken, account.get())) {
                String accessToken = jwtService.generateAccessToken(account.get());

                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void checkAccountValidity(Optional<AuthenticatedEmployee> optionalAccount)
            throws AuthenticationException, LoginException {
        if (optionalAccount.isEmpty()) {
            throw new LoginException();
        }
        AuthenticatedEmployee account = optionalAccount.get();

        if (!account.isEnabled()) {
            throw new DisabledException(
                    "Your account is disabled. Please activate it via link on email: " + account.getEmail()
            );
        }

        if (!account.isNonLocked()) {
            throw new LockedException(
                    "Your account is locked. Please please contact support."
            );
        }
    }

    private AuthenticationResponse authenticateAdmin() {
        AuthenticatedEmployee admin = new AuthenticatedEmployee();
        String adminName = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        admin.setEmail(adminName);
        admin.setPassword(adminPassword);
        admin.setRole(EmployeeAdministrativeRole.HEAD_MANAGER);

        Tokens tokens = jwtService.generateTokens(admin);
        return AuthenticationResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .build();
    }

}
