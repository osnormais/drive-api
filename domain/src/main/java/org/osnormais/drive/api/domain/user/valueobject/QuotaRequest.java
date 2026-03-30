package org.osnormais.drive.api.domain.user.valueobject;

import static java.util.Objects.isNull;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record QuotaRequest(Quota requestedQuota, Instant requestedAt) implements ValueObject {

    public static QuotaRequest create(Quota requestedQuota) {
        return new QuotaRequest(requestedQuota, Instant.now());
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(requestedQuota))
            handler.append(ValidationError.with("'QuotaRequest.requestedQuota' cannot be null."));
        else
            requestedQuota.validate(handler);

        if (isNull(requestedAt))
            handler.append(ValidationError.with("'QuotaRequest.requestedAt' cannot be null."));

    }

}
