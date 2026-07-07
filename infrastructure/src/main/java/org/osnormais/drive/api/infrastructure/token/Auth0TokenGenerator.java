package org.osnormais.drive.api.infrastructure.token;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class Auth0TokenGenerator implements TokenGenerator {

    private final Algorithm algorithm;

    public Auth0TokenGenerator(final AlgorithmProvider algorithmProvider) {
        this.algorithm = requireNonNull(algorithmProvider.provide());
    }

    @Override
    public String generate(final Map<String, Object> claims, final Instant expiresAt) {

        return JWT
                .create()
                .withPayload(claims)
                .withExpiresAt(expiresAt)
                .sign(algorithm);

    }

}
