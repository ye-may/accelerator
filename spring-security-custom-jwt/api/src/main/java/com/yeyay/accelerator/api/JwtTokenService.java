package com.yeyay.accelerator.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtTokenService {
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 6;
    private final AeadAlgorithm encryptionAlgorithm = Jwts.ENC.A128GCM;
    private final SecretKey encryptionKey = encryptionAlgorithm.key().build();

    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .encryptWith(encryptionKey, encryptionAlgorithm)
                .compact();
    }

    public void validateToken(String token) {
        Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
        if (expirationDate.before(new Date())) {
            throw new ExpiredJwtException(null, null, "Token is expired");
        }
    }

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = Jwts.parser()
                    .decryptWith(encryptionKey)
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload();
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new SecurityException("Unable to parse token", e);
        }
    }
}
