package org.osnormais.drive.api.infrastructure.folder.data.rest;

import static java.util.Objects.isNull;

import java.util.List;

import org.osnormais.drive.api.application.usecase.folder.retrieve.get.root.GetRootFolderOutput;

public interface FolderResponsePresenter {

    static GetFolderResponse map(final GetRootFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                Boolean.TRUE,
                output.ownerId(),
                output.name(),
                mapSubFolders(output.subFolders()),
                mapFiles(output.files()),
                output.createdAt(),
                output.updatedAt());
    }

    static List<SubFolder> mapSubFolders(final List<GetRootFolderOutput.SubFolder> subFolders) {
        return isNull(subFolders) ? List.of()
                : subFolders
                        .stream()
                        .map(subFolder -> new SubFolder(subFolder.id(), subFolder.name()))
                        .toList();
    }

    static List<FolderFile> mapFiles(final List<GetRootFolderOutput.File> files) {
        return isNull(files) ? List.of()
                : files
                        .stream()
                        .map(file -> new FolderFile(
                                file.id(),
                                file.name(),
                                file.sizeInBytes(),
                                file.contentType(),
                                file.createdAt()))
                        .toList();
    }

}
