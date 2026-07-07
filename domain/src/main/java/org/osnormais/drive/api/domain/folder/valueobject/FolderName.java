package org.osnormais.drive.api.domain.folder.valueobject;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.regex.Pattern;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record FolderName(String value) implements ValueObject {

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

    private static final String INVALID_CHARACTERS_REGEX = "[./\\\\:*?\"<>|]";
    private static final Pattern INVALID_CHARACTERS_PATTERN = Pattern.compile(INVALID_CHARACTERS_REGEX);

    public static FolderName of(final String value) {
        return new FolderName(value);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(value)) {
            handler.append(new ValidationError("'FolderName.value' cannot be null."));
            return;
        }

        if (value.trim().isEmpty())
            handler.append(new ValidationError("'FolderName.value' cannot be empty."));

        if (value.trim().length() < MIN_LENGTH || value.trim().length() > MAX_LENGTH)
            handler.append(
                    new ValidationError(
                            "'FolderName.value' must be between "
                                    + MIN_LENGTH
                                    + " and "
                                    + MAX_LENGTH
                                    + " characters."));

        if (containsInvalidCharacters(value.trim()))
            handler.append(new ValidationError("'FolderName.value' contains invalid characters."));

        if (isReservedName(value.trim()))
            handler.append(ValidationError.with("'FolderName.value' cannot be a reserved name: " + value.trim()));

    }

    private static boolean containsInvalidCharacters(String input) {
        return INVALID_CHARACTERS_PATTERN
                .matcher(input)
                .find();
    }

    private static boolean isReservedName(final String name) {
        return List.of(RESERVED_NAMES)
                .stream()
                .anyMatch(reservedName -> reservedName.equalsIgnoreCase(name.trim()));
    }

}
