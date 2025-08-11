package ua.knu.knudev.iccsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.iccsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.iccsecurity.security.AuthenticatedEmployeeDetails;
import ua.knu.knudev.iccsecurity.utils.JWTSigningKeyProvider;
import ua.knu.knudev.iccsecurityapi.dto.Tokens;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final Integer accessTokenExpirationInMillis;
    private final Integer refreshTokenExpirationInMillis;
    private final String issuerName;
    private final EmployeeApi employeeApi;
    private final JWTSigningKeyProvider jwtSigningKeyProvider;

    public JwtService(
            @Value("${application.jwt.expiration}") Integer accessTokenExpirationInMillis,
            @Value("${application.jwt.refresh-token.expiration}") Integer refreshTokenExpirationInMillis,
            @Value("${application.jwt.issuer}") String issuerName,
            JWTSigningKeyProvider jwtSigningKeyProvider,
            EmployeeApi employeeApi) {
        this.accessTokenExpirationInMillis = accessTokenExpirationInMillis;
        this.refreshTokenExpirationInMillis = refreshTokenExpirationInMillis;
        this.issuerName = issuerName;
        this.jwtSigningKeyProvider = jwtSigningKeyProvider;
        this.employeeApi = employeeApi;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtSigningKeyProvider.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    public Set<String> extractAccountRoles(String token) {
        try {
            @SuppressWarnings("unchecked")
            List<String> roles = parseClaims(token).get("roles", List.class);

            return roles == null ? Collections.emptySet() : roles.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

        } catch (JwtException | IllegalArgumentException e) {
            throw new SecurityException("Invalid or expired JWT token", e);
        }
    }

    public boolean isAccessToken(String token) {
        Boolean isAccessToken = parseClaims(token).get("isacct", Boolean.class);
        return isAccessToken != null && isAccessToken;
    }

    public boolean isAccessTokenValid(String token, AuthenticatedEmployeeDetails authenticatedEmployeeDetails) {
        Date expiryDate = extractClaim(token, Claims::getExpiration);
        boolean isTokenExpired = expiryDate.before(new Date());
        String email = extractEmail(token);

        return StringUtils.equals(email, authenticatedEmployeeDetails.getEmail()) && !isTokenExpired;
    }

    public Tokens generateTokens(AuthenticatedEmployeeDetails authenticatedEmployeeDetails) {
        return Tokens.builder()
                .accessToken(generateAccessToken(authenticatedEmployeeDetails))
                .refreshToken(generateRefreshToken(authenticatedEmployeeDetails))
                .build();
    }

    public String generateAccessToken(AuthenticatedEmployeeDetails authenticatedEmployeeDetails) {
        AuthenticatedEmployee employee = (AuthenticatedEmployee) authenticatedEmployeeDetails;
        Map<String, Object> extraClaims = buildExtraClaims(true, employee);
        return buildToken(extraClaims, authenticatedEmployeeDetails, accessTokenExpirationInMillis);
    }

    public String generateRefreshToken(AuthenticatedEmployeeDetails authenticatedEmployeeDetails) {
        Map<String, Object> extraClaims = buildExtraClaims(false, authenticatedEmployeeDetails);
        return buildToken(extraClaims, authenticatedEmployeeDetails, refreshTokenExpirationInMillis);
    }

    private String buildToken(
            Map<String, Object> claims,
            AuthenticatedEmployeeDetails authenticatedEmployeeDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(authenticatedEmployeeDetails.getEmail())
                .issuer(issuerName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtSigningKeyProvider.getSigningKey())
                .compact();
    }

    private Map<String, Object> buildExtraClaims(
            boolean isAccessToken,
            AuthenticatedEmployeeDetails authenticatedEmployeeDetails
    ) {
        Map<String, Object> extraClaimsMap = new HashMap<>();
        extraClaimsMap.put("isacct", isAccessToken);

        Set<String> employeeRoles = authenticatedEmployeeDetails.getRoles().stream()
                .filter(Objects::nonNull)
                .map(EmployeeAdministrativeRole::name)
                .collect(Collectors.toSet());
        extraClaimsMap.put("roles", employeeRoles);
        extraClaimsMap.put("userid", authenticatedEmployeeDetails.getId());

        String email = authenticatedEmployeeDetails.getEmail();
        UUID employeeId = employeeApi.getEmployeeIdByEmail(email);

        extraClaimsMap.put("employeeId", employeeId);
        return extraClaimsMap;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSigningKeyProvider.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
