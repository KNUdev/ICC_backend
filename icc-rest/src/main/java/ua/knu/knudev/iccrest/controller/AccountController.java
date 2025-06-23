package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.request.AccountCredentialsUpdateRequest;
import ua.knu.knudev.employeemanagerapi.request.AccountReceivingRequest;
import ua.knu.knudev.employeemanagerapi.response.AccountReceivingResponse;
import ua.knu.knudev.iccsecurityapi.api.AuthenticationServiceApi;
import ua.knu.knudev.iccsecurityapi.request.AuthenticationRequest;
import ua.knu.knudev.iccsecurityapi.response.AuthenticationResponse;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final EmployeeApi employeeApi;
    private final AuthenticationServiceApi authenticationServiceApi;

    @Operation(
            summary = "Register a new account",
            description = """
                    This endpoint allows users to register a new account.
                    The request must include all required fields, such as username, email, and password.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Account successfully registered.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccountReceivingResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input provided.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountReceivingResponse registerAccount(
            @Valid @ModelAttribute @Parameter(
                    name = "Account creation request",
                    description = "Account registration details",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = AccountReceivingRequest.class)
            ) AccountReceivingRequest registrationRequest) {
        return employeeApi.register(registrationRequest);
    }

    @Operation(
            summary = "Update account credentials",
            description = "Updates login credentials (e.g. username or password) for the authenticated account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Credentials updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountReceivingResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PutMapping("/credentials/update")
    public AccountReceivingResponse updateAccountCredentials(
            @Parameter(
                    name = "Account credentials update request",
                    description = "Account credentials update request",
                    in = ParameterIn.QUERY,
                    schema = @Schema(implementation = AccountReceivingRequest.class),
                    required = true
            ) @Valid AccountCredentialsUpdateRequest request
    ) {
        return employeeApi.updateCredentials(request);
    }

    @Operation(
            summary = "Authenticate user",
            description = "This endpoint allows the user to authenticate by providing their credentials" +
                    " (username and password)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid authentication request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access - Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Parameter(
                    name = "Authentication request",
                    description = "Authentication request containing the user's credentials",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(implementation = AuthenticationRequest.class)
            ) AuthenticationRequest authenticationRequest
    ) throws LoginException {
        return ResponseEntity.ok(authenticationServiceApi.authenticate(authenticationRequest));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Refresh the access token using a valid refresh token provided in the Authorization header. "
                    + "The header must be in the format 'Bearer <refreshToken>'. On success, returns a new access " +
                    "token along with the provided refresh token.",
            parameters = {
                    @Parameter(
                            name = HttpHeaders.AUTHORIZATION,
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Bearer token containing the refresh token",
                            schema = @Schema(
                                    type = "string",
                                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY" +
                                            "3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeK" +
                                            "KF2QT4fwpMeJf36POk6yJV_adQssw5c"
                            ))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - either an access token was provided instead of a refresh token," +
                            " or the token is invalid.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid Authorization header, or the provided " +
                            "token has expired.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationServiceApi.refreshToken(request, response);
    }

}
