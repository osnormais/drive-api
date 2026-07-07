package org.osnormais.drive.api.application.usecase.folder.retrieve.get.root;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.Folder;

public record GetRootFolderOutput(
        UUID id,
        String name,
        List<GetRootFolderOutput.SubFolder> subFolders,
        List<GetRootFolderOutput.File> files,
        UUID ownerId,
        Instant createdAt,
        Instant updatedAt) {

    public static GetRootFolderOutput from(
            final Folder folder,
            final Set<Folder> subFolders,
            final Set<org.osnormais.drive.api.domain.file.File> files) {

        return new GetRootFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                subFolders.stream().map(GetRootFolderOutput.SubFolder::from).toList(),
                files.stream().map(GetRootFolderOutput.File::from).toList(),
                folder.getOwner().getValue(),
                folder.getCreatedAt(),
                folder.getUpdatedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetRootFolderOutput.SubFolder from(final Folder subFolder) {
            return new SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
        }

    }

    public static record File(
            UUID id,
            String name,
            Long sizeInBytes,
            String contentType,
            Instant createdAt) {

        public static GetRootFolderOutput.File from(org.osnormais.drive.api.domain.file.File file) {
            return new GetRootFolderOutput.File(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getSize().bytes(),
                    file.getContent().type(),
                    file.getCreatedAt());
        }
    }

}
