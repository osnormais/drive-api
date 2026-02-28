package org.osnormais.drive.api.domain;

import org.osnormais.drive.api.domain.validation.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler handler);

}
