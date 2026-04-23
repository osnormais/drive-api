package org.osnormais.drive.api.domain.entitlement.grant;

import java.time.Instant;
import java.util.Optional;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.entitlement.quota.Quota;

public record QuotaGrant<Q extends Quota>(Q quota, Optional<Instant> expiresAt) implements ValueObject {

    public static <Q extends Quota> QuotaGrant<Q> of(final Q quota, final Optional<Instant> expiresAt) {
        return new QuotaGrant<>(quota, expiresAt);
    }

    public static <Q extends Quota> QuotaGrant<Q> permanent(final Q quota) {
        return new QuotaGrant<>(quota, Optional.empty());
    }

    public Boolean isActive() {
        return expiresAt.isEmpty() || Instant.now().isBefore(expiresAt.get());
    }

}
