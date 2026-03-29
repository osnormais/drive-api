package org.osnormais.drive.api.domain.file.valueobject;

import org.osnormais.drive.api.domain.ValueObject;

public record Content(String type) implements ValueObject {

    public static Content of(final String type) {
        return new Content(type);
    }

}
