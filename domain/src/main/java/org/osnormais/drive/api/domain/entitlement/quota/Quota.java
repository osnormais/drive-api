package org.osnormais.drive.api.domain.entitlement.quota;

import static java.util.Objects.isNull;

import java.util.Optional;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public interface Quota extends ValueObject {

    Type type();

    Optional<Amount> amount();

    Boolean isUnlimited();

    @Override
    default void validate(final ValidationHandler handler) {

        if (isNull(type()))
            handler.append(new ValidationError("Quota.type must be provided"));

        if (isNull(amount()))
            handler.append(new ValidationError("Quota.amount cannot be null"));

        if (amount().isEmpty() && !isUnlimited())
            handler.append(new ValidationError("Quota.amount must be provided when quota is not unlimited"));

        amount().ifPresent(amount -> amount.validate(handler));

    }

}
