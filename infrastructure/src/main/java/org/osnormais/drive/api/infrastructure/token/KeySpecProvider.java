package org.osnormais.drive.api.infrastructure.token;

import java.security.spec.KeySpec;

@FunctionalInterface
public interface KeySpecProvider {

    KeySpec provide();

}
