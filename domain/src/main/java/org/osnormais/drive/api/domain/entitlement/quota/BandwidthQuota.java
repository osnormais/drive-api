package org.osnormais.drive.api.domain.entitlement.quota;

import java.util.Optional;

public record BandwidthQuota(Optional<Amount> amount, Boolean isUnlimited) implements Quota {

    @Override
    public Type type() {
        return Type.BYTES_PER_SECOND;
    }

    public static BandwidthQuota with(final Optional<Amount> amount, final Boolean isUnlimited) {
        return new BandwidthQuota(amount, isUnlimited);
    }

    public static BandwidthQuota of(final Amount amount) {
        return new BandwidthQuota(Optional.of(amount), false);
    }

    public static BandwidthQuota unlimited() {
        return new BandwidthQuota(Optional.empty(), true);
    }

}
