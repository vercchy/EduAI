package finku.ukim.mk.eduai.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;


    public String generateToken(String username, String roleName, String email) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", roleName)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseJwtClaims(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parseJwtClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Jws<Claims> parseJwtClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
    }

    public String extractRole(String token) {
        return parseJwtClaims(token).getBody().get("role", String.class);
    }
    public String extractEmail(String token) {
        return parseJwtClaims(token).getBody().get("email", String.class);
    }


}
