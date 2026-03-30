package org.osnormais.drive.api.domain.exception;

import java.util.List;

import org.osnormais.drive.api.domain.user.valueobject.Quota;

public class QuotaExceededException extends SilentDomainException {

    private QuotaExceededException(final Quota actualQuota) {
        super(
                "Quota exceeded.",
                List.of(DomainException.Error.with(
                        "You have exceeded your current quota of "
                                + actualQuota.bytes()
                                + " bytes. Please free up some space or request a quota increase.")));
    }

    public static QuotaExceededException with(final Quota actualQuota) {
        return new QuotaExceededException(actualQuota);
    }
}
