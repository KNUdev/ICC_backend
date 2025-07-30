package ua.knu.knudev.iccsecurity.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import ua.knu.knudev.iccsecurity.security.Auth403EntryPoint;
import ua.knu.knudev.iccsecurity.security.filters.CorsFilter;
import ua.knu.knudev.iccsecurity.security.filters.JWTValidityFilter;
import ua.knu.knudev.iccsecurity.security.filters.JwtAuthenticationFilter;

import static ua.knu.knudev.iccsecurity.security.config.UrlRegistry.WHITE_LIST_URLS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JWTValidityFilter jwtValidityFilter;
    private final Auth403EntryPoint auth403EntryPoint;
    private final CorsFilter corsFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(WHITE_LIST_URLS).permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/components/**").permitAll()
                            .anyRequest().permitAll();
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtValidityFilter, SecurityContextHolderFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(corsFilter, JWTValidityFilter.class)
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(handler ->
                        handler.authenticationEntryPoint(auth403EntryPoint))
                .logout(logout ->
                        logout.logoutSuccessHandler((
                                request,
                                response,
                                authentication) ->
                                SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
