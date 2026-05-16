package org.osnormais.drive.api.domain.file.valueobject;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record FileSize(long bytes) implements ValueObject {

    public static FileSize of(final long bytes) {
        return new FileSize(bytes);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (bytes <= 0)
            handler.append(ValidationError.with("FileSize.bytes must be greater than 0"));

    }

}
