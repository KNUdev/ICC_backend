package ua.knu.knudev.iccsecurity.security.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.knu.knudev.iccsecurity.utils.JWTFiltersHelper;
import ua.knu.knudev.iccsecurity.utils.JWTSigningKeyProvider;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTValidityFilter extends OncePerRequestFilter {

    private final JWTFiltersHelper jwtFiltersHelper;
    private final JWTSigningKeyProvider jwtSigningKeyProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtHeader = jwtFiltersHelper.extractJWTHeader(request);
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")
                && !StringUtils.containsIgnoreCase(request.getServletPath(), "refresh-token")) {
            String jwt = jwtHeader.substring(7);

            try {
                SecretKey jwtSignInKey = jwtSigningKeyProvider.getSigningKey();
                Jwts.parser()
                        .verifyWith(jwtSignInKey)
                        .build()
                        .parseSignedClaims(jwt);
            } catch (JwtException ex) {
                if (ex instanceof ExpiredJwtException) {
                    jwtFiltersHelper.writeMessageInResponse(
                            response,
                            401,
                            "Your token has expired"
                    );
                    return;
                }

                jwtFiltersHelper.writeMessageInResponse(
                        response,
                        401,
                        "Your JWT token is invalid"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
