package org.osnormais.drive.api.domain.exception;

import java.util.List;

public class InconsistentStateException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "[%s] Inconsistent state";

    private InconsistentStateException(Class<?> inconsistentClass, List<Error> errors) {
        super(MESSAGE_TEMPLATE.formatted(inconsistentClass.getSimpleName()), errors);
    }

    public static InconsistentStateException create(Class<?> inconsistentClass, List<Error> errors) {
        return new InconsistentStateException(inconsistentClass, errors);
    }

}
