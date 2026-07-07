package org.osnormais.drive.api.domain.entitlement.quota;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record Amount(Long value) implements ValueObject {

    public static Amount of(final Long value) {
        return new Amount(value);
    }

    public static Amount zero() {
        return Amount.of(0L);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(value))
            handler.append(new ValidationError("Amount value cannot be null"));
        else if (value < 0L)
            handler.append(new ValidationError("Amount value cannot be negative"));

    }

    public Amount remaining(final Amount used) {
        return Amount.of(Math.max(0L, value - used.value));
    }

    public Amount add(final Amount other) {
        return Amount.of(value + other.value);
    }

}
