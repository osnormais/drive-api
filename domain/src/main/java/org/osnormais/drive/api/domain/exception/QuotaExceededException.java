package org.osnormais.drive.api.domain.exception;

import java.util.List;

import org.osnormais.drive.api.domain.entitlement.quota.Quota;
import org.osnormais.drive.api.domain.entitlement.quota.Type;

public class QuotaExceededException extends SilentDomainException {

    private QuotaExceededException(final Quota actualQuota) {
        super(
                "Quota exceeded.",
                List.of(DomainException.Error.with(
                        buildMessage(actualQuota))));
    }

    public static QuotaExceededException with(final Quota actualQuota) {
        return new QuotaExceededException(actualQuota);
    }

    private static String buildMessage(final Quota quota) {
        final String unit = getUnit(quota.type());
        return String.format(
                "You have exceeded your current quota of %s %s. Please request a quota increase.",
                quota.amount(),
                unit);
    }

    private static String getUnit(final Type type) {
        return switch (type) {
            case BYTES -> "bytes";
            case TOTAL_COUNT -> "total items";
            case PARALLEL_OPERATIONS -> "parallel operations";
            case BYTES_PER_SECOND -> "bps";
        };
    }
}
