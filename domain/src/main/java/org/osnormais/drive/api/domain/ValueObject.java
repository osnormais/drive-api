package org.osnormais.drive.api.domain;

import org.osnormais.drive.api.domain.validation.Validatable;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public interface ValueObject extends Validatable {

    default void validate(ValidationHandler handler) {
    };

}
