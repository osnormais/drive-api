package org.osnormais.drive.api.infrastructure.folder.data.rest;

import static java.util.Objects.isNull;

import java.util.List;

import org.osnormais.drive.api.application.usecase.folder.retrieve.get.GetFolderOutput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.root.GetRootFolderOutput;
import org.osnormais.drive.api.domain.folder.FolderType;

public interface FolderResponsePresenter {

    static GetFolderResponse map(final GetRootFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                output.id(),
                FolderType.ROOT,
                output.ownerId(),
                output.name(),
                mapRootSubFolders(output.subFolders()),
                mapRootFiles(output.files()),
                output.createdAt(),
                output.updatedAt());
    }

    static GetFolderResponse map(final GetFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                output.parentId(),
                output.type(),
                output.ownerId(),
                output.name(),
                mapSubFolders(output.subFolders()),
                mapFiles(output.files()),
                output.createdAt(),
                output.updatedAt());
    }

    static List<SubFolder> mapSubFolders(final List<GetFolderOutput.SubFolder> subFolders) {
        return isNull(subFolders) ? List.of()
                : subFolders
                        .stream()
                        .map(subFolder -> new SubFolder(subFolder.id(), subFolder.name()))
                        .toList();
    }

    static List<SubFolder> mapRootSubFolders(final List<GetRootFolderOutput.SubFolder> subFolders) {
        return isNull(subFolders) ? List.of()
                : subFolders
                        .stream()
                        .map(subFolder -> new SubFolder(subFolder.id(), subFolder.name()))
                        .toList();
    }

    static List<FolderFile> mapFiles(final List<GetFolderOutput.File> files) {
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

    static List<FolderFile> mapRootFiles(final List<GetRootFolderOutput.File> files) {
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
