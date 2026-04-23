package org.osnormais.drive.api.domain.entitlement.quota;

import java.util.Optional;

public record ParallelOperationsQuota(Optional<Amount> amount, Boolean isUnlimited) implements Quota {

    @Override
    public Type type() {
        return Type.PARALLEL_OPERATIONS;
    }

    public static ParallelOperationsQuota with(final Optional<Amount> amount, final Boolean isUnlimited) {
        return new ParallelOperationsQuota(amount, isUnlimited);
    }

    public static ParallelOperationsQuota of(final Amount amount) {
        return new ParallelOperationsQuota(Optional.of(amount), false);
    }

    public static ParallelOperationsQuota unlimited() {
        return new ParallelOperationsQuota(Optional.empty(), true);
    }

}