package org.osnormais.drive.api.infrastructure.token;

import static java.util.Objects.requireNonNull;

import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class PKCS8KeySpecProvider implements KeySpecProvider {

    private final byte[] encodedKey;

    public PKCS8KeySpecProvider(final byte[] encodedKey) {
        this.encodedKey = Arrays.copyOf(requireNonNull(encodedKey), requireNonNull(encodedKey).length);
    }

    @Override
    public KeySpec provide() {
        return new PKCS8EncodedKeySpec(encodedKey);
    }

}
