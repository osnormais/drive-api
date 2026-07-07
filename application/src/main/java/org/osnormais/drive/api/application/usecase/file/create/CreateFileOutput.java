package org.osnormais.drive.api.application.usecase.file.create;

import java.util.UUID;

import org.osnormais.drive.api.domain.file.File;

public record CreateFileOutput(UUID id) {

    public static CreateFileOutput of(final File file) {
        return new CreateFileOutput(file.getId().getValue());
    }

}
