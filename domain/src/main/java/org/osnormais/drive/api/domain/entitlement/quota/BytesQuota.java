package org.osnormais.drive.api.domain.entitlement.quota;

import java.util.Optional;

public record BytesQuota(Optional<Amount> amount, Boolean isUnlimited) implements Quota {

    @Override
    public Type type() {
        return Type.BYTES;
    }

    public static BytesQuota with(final Optional<Amount> amount, final Boolean isUnlimited) {
        return new BytesQuota(amount, isUnlimited);
    }

    public static BytesQuota of(final Amount amount) {
        return new BytesQuota(Optional.of(amount), false);
    }

    public static BytesQuota unlimited() {
        return new BytesQuota(Optional.empty(), true);
    }

    public BytesQuota remaining(final BytesQuota used) {

        if (used.isUnlimited())
            return BytesQuota.of(Amount.of(0L));

        if (isUnlimited())
            return this;

        if (amount().isEmpty())
            return BytesQuota.of(Amount.of(0L));

        final Amount remainingAmount = amount()
                .orElse(Amount.of(0L))
                .remaining(used.amount().orElse(Amount.of(0L)));

        return BytesQuota.of(remainingAmount);

    }

    public BytesQuota add(final BytesQuota other) {

        if (isUnlimited() || other.isUnlimited())
            return BytesQuota.unlimited();

        final Amount totalAmount = amount()
                .orElse(Amount.of(0L))
                .add(other.amount().orElse(Amount.of(0L)));

        return BytesQuota.of(totalAmount);

    }

}
