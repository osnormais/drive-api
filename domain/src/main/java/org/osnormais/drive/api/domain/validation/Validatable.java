package org.osnormais.drive.api.domain.validation;

import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler handler);

}
