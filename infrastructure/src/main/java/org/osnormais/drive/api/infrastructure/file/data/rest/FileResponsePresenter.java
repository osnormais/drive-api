package org.osnormais.drive.api.infrastructure.file.data.rest;

import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileOutput;
import org.osnormais.drive.api.application.usecase.file.retrieve.list.ListFileOutput;

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

    static ListFileItemResponse map(final ListFileOutput.Item item) {
        return new ListFileItemResponse(
                item.id(),
                item.name(),
                item.sizeInBytes(),
                item.contentType(),
                item.folderId(),
                item.ownerId(),
                item.creatorId(),
                item.createdAt(),
                item.updatedAt());
    }

}
