package br.com.links_manager_back_springboot.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class JWTService {

    @Value("${jwt-token-secret}")
    private String jwtTokenSecret;

    @Value("${jwt-token-issuer}")
    private String jwtTokenIssuer;

    public String generateToken(UUID subject) {
        Algorithm algorithm = Algorithm.HMAC256(jwtTokenSecret);
        Instant expiresAt = Instant.now().plus(Duration.ofHours(2));

        return JWT
                .create()
                .withIssuer(jwtTokenIssuer)
                .withSubject(subject.toString())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public String validateToken(String token) {
        token = token.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(jwtTokenSecret);

        try {
            return JWT
                    .require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

}
