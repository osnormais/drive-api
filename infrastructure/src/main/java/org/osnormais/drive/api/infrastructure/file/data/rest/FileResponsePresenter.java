package org.osnormais.drive.api.infrastructure.file.data.rest;

import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileOutput;

public interface FileResponsePresenter {

    static GetFileResponse map(final GetFileOutput output) {
        return new GetFileResponse(
                output.id(),
                output.name(),
                output.sizeInBytes(),
                output.contentType(),
                output.checksumValue(),
                output.checksumAlgorithm(),
                output.folderId(),
                output.ownerId(),
                output.creatorId(),
                output.createdAt(),
                output.updatedAt());
    }

}
