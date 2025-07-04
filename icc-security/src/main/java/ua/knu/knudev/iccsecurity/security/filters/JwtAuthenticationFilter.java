package ua.knu.knudev.iccsecurity.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.iccsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.iccsecurity.security.AuthenticatedEmployeeDetails;
import ua.knu.knudev.iccsecurity.service.JwtService;
import ua.knu.knudev.iccsecurity.utils.JWTFiltersHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static ua.knu.knudev.iccsecurity.security.config.UrlRegistry.AUTH_EXCLUDED_URLS;
import static ua.knu.knudev.iccsecurity.security.config.UrlRegistry.AUTH_URL;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JWTFiltersHelper jwtFiltersHelper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (isPublicUrlRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = jwtFiltersHelper.extractJWTHeader(request);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String accountUsername = jwtService.extractEmail(jwt);
        if (accountUsername != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!jwtService.isAccessToken(jwt)) {
                jwtFiltersHelper.writeMessageInResponse(
                        response,
                        403,
                        "Please enter an access token"
                );
                return;
            }
            AuthenticatedEmployeeDetails userDetails = buildUserDetails(accountUsername, jwt);

            if (jwtService.isAccessTokenValid(jwt, userDetails)) {
                setAuthentication(userDetails, request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private AuthenticatedEmployeeDetails buildUserDetails(String accountUsername, String jwt) {
        Set<String> roles = jwtService.extractAccountRoles(jwt);
        String stringRole = roles.stream().findFirst().orElse(null);

        EmployeeAdministrativeRole employeeAdministrativeRole = Arrays.stream(EmployeeAdministrativeRole.values())
                .filter(role ->
                        role.name().equalsIgnoreCase(stringRole))
                .findFirst()
                .orElse(null);

        return AuthenticatedEmployee.builder()
                .email(accountUsername)
                .role(employeeAdministrativeRole)
                .build();
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isPublicUrlRequest(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.contains(AUTH_URL) && !AUTH_EXCLUDED_URLS.contains(servletPath);
    }

}
