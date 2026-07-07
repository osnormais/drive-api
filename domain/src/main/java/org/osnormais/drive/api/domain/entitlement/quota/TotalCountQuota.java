package org.osnormais.drive.api.domain.entitlement.quota;

import java.util.Optional;

public record TotalCountQuota(Optional<Amount> amount, Boolean isUnlimited) implements Quota {

    @Override
    public Type type() {
        return Type.TOTAL_COUNT;
    }

    public TotalCountQuota(Amount amount) {
        this(Optional.of(amount), false);
    }

    public static TotalCountQuota with(final Optional<Amount> amount, final Boolean isUnlimited) {
        return new TotalCountQuota(amount, isUnlimited);
    }

    public static TotalCountQuota unlimited() {
        return new TotalCountQuota(Optional.empty(), true);
    }

}
