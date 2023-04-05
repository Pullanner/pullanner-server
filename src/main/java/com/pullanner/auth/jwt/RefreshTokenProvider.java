package com.pullanner.auth.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.security.core.Authentication;

public class RefreshTokenProvider implements TokenProvider<RefreshToken> {

    private final Key secretKey;
    private final SignatureAlgorithm signatureAlgorithm;
    private final long duration;

    public RefreshTokenProvider(String secret, SignatureAlgorithm signatureAlgorithm, long duration) {
        this.secretKey = getSecretKey(secret);
        this.signatureAlgorithm = signatureAlgorithm;
        this.duration = duration;
    }

    private Key getSecretKey(String secret) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public RefreshToken createToken(String email) {
        return new RefreshToken(email, secretKey, signatureAlgorithm, duration);
    }

    public RefreshToken convertToObject(String token) {
        return new RefreshToken(token, secretKey);
    }
}
