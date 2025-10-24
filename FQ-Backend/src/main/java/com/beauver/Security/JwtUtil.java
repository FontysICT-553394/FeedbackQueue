package com.beauver.Security;

import com.beauver.Classes.Result;
import com.beauver.Enums.StatusCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class JwtUtil {

    @ConfigProperty(name = "jwt.secret")
    String secret;

    @ConfigProperty(name = "jwt.expiration")
    long expirationMs;

    private SecretKey getSigningKey() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("jwt.secret must be provided");
        }
        return hmacShaKeyFor(secret.getBytes(UTF_8));
    }

    /**
     * Generates a JWT token for the given user ID.
     * @param id The (user) ID to include in the token.
     * @return The generated JWT token as a String.
     */
    public String generateToken(String id) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the user ID from the Authorization header.
     * @param authorization The Authorization header containing the Bearer token.
     * @return The user ID if the token is valid;
     */
    public String getIdFromHeader(String authorization){
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return new Result<>(StatusCodes.UNAUTHORIZED).toJson();
        }

        String token = authorization.substring("Bearer ".length());
        return getIdFromToken(token);
    }

    /**
     * Extracts the user ID from the JWT token.
     * @param token The JWT token.
     * @return The user ID if the token is valid;
     */
    public String getIdFromToken(String token) {
        SecretKey key = getSigningKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Validates the JWT token.
     * @param token The JWT token to validate.
     * @return true if the token is valid; false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = getSigningKey();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
