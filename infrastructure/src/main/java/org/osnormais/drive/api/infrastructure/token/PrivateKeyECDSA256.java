package org.osnormais.drive.api.infrastructure.token;

import static java.util.Objects.requireNonNull;

import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.KeySpec;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;

@Component
@ConditionalOnProperty(name = "application.token.signature.algorithm", havingValue = "ECDSA256")
public class PrivateKeyECDSA256 implements AlgorithmProvider {

    private final KeySpec keySpec;

    PrivateKeyECDSA256(final KeySpecProvider keySpecProvider) {
        this.keySpec = requireNonNull(keySpecProvider).provide();
    }

    @Override
    public Algorithm provide() {

        try {
            final KeyFactory kf = KeyFactory.getInstance("EC");
            final ECPrivateKey privateKey = (ECPrivateKey) kf.generatePrivate(keySpec);
            return Algorithm.ECDSA256(privateKey);
        } catch (Throwable e) {
            throw new RuntimeException(e); // TODO: Handle this properly
        }

    }

}
