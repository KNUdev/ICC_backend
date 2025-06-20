package ua.knu.knudev.iccsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.knu.knudev.iccsecurity.exception.AccountAuthException;
import ua.knu.knudev.iccsecurity.repository.AuthenticatedEmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmailUserDetailsService implements UserDetailsService {

    private final AuthenticatedEmployeeRepository authenticatedEmployeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticatedEmployeeRepository.findAuthenticatedEmployeesByEmail(username)
                .orElseThrow(() -> new AccountAuthException(username));
    }
}
