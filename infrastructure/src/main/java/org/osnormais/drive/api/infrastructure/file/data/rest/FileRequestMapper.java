package org.osnormais.drive.api.infrastructure.file.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.file.create.CreateFileInput;

public interface FileRequestMapper {

    static CreateFileInput map(final CreateFileRequest request, final UUID creatorId) {
        return new CreateFileInput(
                creatorId,
                request.parentFolderId(),
                request.name(),
                request.contentType(),
                request.sizeInBytes(),
                request.checksumValue(),
                request.checksumAlgorithm());
    }

}
