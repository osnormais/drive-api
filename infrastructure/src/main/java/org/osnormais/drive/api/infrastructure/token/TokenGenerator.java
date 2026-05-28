package org.osnormais.drive.api.infrastructure.token;

import java.time.Instant;
import java.util.Map;

@FunctionalInterface
public interface TokenGenerator {

    String generate(final Map<String, Object> claims, final Instant expiresAt);

}
