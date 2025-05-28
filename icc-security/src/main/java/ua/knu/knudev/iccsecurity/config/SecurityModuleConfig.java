package ua.knu.knudev.iccsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan("ua.knu.knudev.iccsecurity")
//@EntityScan("ua.knu.knudev.iccsecurity.domain")
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableWebMvc
public class SecurityModuleConfig {
    //todo change in future
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
