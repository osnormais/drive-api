package org.osnormais.drive.api.domain.file.valueobject;

import java.util.List;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record FileName(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;
    private static final String[] RESERVED_NAMES = {
            "CON",
            "NUL",
            "PRN",
            "AUX",
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "LPT1",
            "LPT2",
            "LPT3"
    };

    public static FileName of(final String value) {
        return new FileName(value);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {

        if (value == null) {
            aHandler.append(ValidationError.with("'FileName.value' cannot be null."));
            return;
        }

        if (value.trim().isEmpty())
            aHandler.append(ValidationError.with("'FileName.value' cannot be empty."));

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH)
            aHandler.append(
                    ValidationError.with(
                            "'FileName.value' must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

        if (isReservedName(value.trim()))
            aHandler.append(ValidationError.with("'FileName.value' cannot be a reserved name: " + value.trim()));
    }

    private static boolean isReservedName(final String name) {
        return List.of(RESERVED_NAMES)
                .stream()
                .anyMatch(reservedName -> reservedName.equalsIgnoreCase(name));
    }

}
