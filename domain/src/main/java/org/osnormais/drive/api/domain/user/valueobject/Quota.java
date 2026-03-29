package org.osnormais.drive.api.domain.user.valueobject;

import org.osnormais.drive.api.domain.ValueObject;

public record Quota(Long bytes) implements ValueObject {

    public static Quota of(final Long bytes) {
        return new Quota(bytes);
    }

}
