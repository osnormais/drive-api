package org.osnormais.drive.api.domain.user.valueobject;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;

public record QuotaRequest(Quota requestedQuota, Instant requestedAt) implements ValueObject {

    public static QuotaRequest create(Quota requestedQuota) {
        return new QuotaRequest(requestedQuota, Instant.now());
    }

}
