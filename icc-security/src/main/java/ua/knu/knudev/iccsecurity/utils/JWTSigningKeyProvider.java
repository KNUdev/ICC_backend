package ua.knu.knudev.iccsecurity.utils;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * For correct work of authorization/authentication service you need to add
 * JWT_SECRET_KEY=OblBr1u8BlwbqA61vt0yk4TC/mPlvT2O+t2m9SCci6g=  into your
 * env properties
 */

@Component
@Getter
@Scope("singleton")
public class JWTSigningKeyProvider {

    private final SecretKey signingKey;

    public JWTSigningKeyProvider() {
//        String jwtSecretKey = System.getenv("JWT_SECRET_KEY");
        String jwtSecretKey = "OblBr1u8BlwbqA61vt0yk4TC/mPlvT2O+t2m9SCci6g=";
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
}
