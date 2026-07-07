package org.osnormais.drive.api.domain.entitlement.quota;

import static java.util.Objects.isNull;

import java.util.Optional;

public record BandwidthQuota(Optional<Amount> amount) implements Quota {

    public static final Long DEFAULT_THROUGHPUT_LIMIT = 100L;

    @Override
    public Type type() {
        return Type.BYTES_PER_SECOND;
    }

    @Override
    public Optional<Amount> amount() {
        return isNull(amount) ? Optional.of(Amount.of(DEFAULT_THROUGHPUT_LIMIT)) : amount;
    }

    @Override
    public Boolean isUnlimited() {
        return false;
    }

    public static BandwidthQuota with(final Optional<Amount> amount, final Boolean isUnlimited) {
        return new BandwidthQuota(amount);
    }

    public static BandwidthQuota of(final Amount amount) {
        return new BandwidthQuota(Optional.of(amount));
    }

    public static BandwidthQuota unlimited() {
        return new BandwidthQuota(Optional.empty());
    }

}
