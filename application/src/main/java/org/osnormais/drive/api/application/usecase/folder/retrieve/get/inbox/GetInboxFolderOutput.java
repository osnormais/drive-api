package org.osnormais.drive.api.application.usecase.folder.retrieve.get.inbox;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.Folder;

public record GetInboxFolderOutput(
        UUID id,
        String name,
        List<GetInboxFolderOutput.SubFolder> subFolders,
        List<GetInboxFolderOutput.File> files,
        UUID ownerId,
        Instant createdAt,
        Instant updatedAt) {

    public static GetInboxFolderOutput from(
            final Folder folder,
            final Set<Folder> subFolders,
            final Set<org.osnormais.drive.api.domain.file.File> files) {

        return new GetInboxFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                subFolders.stream().map(GetInboxFolderOutput.SubFolder::from).toList(),
                files.stream().map(GetInboxFolderOutput.File::from).toList(),
                folder.getOwner().getValue(),
                folder.getCreatedAt(),
                folder.getUpdatedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetInboxFolderOutput.SubFolder from(final Folder subFolder) {
            return new SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
        }

    }

    public static record File(
            UUID id,
            String name,
            Long sizeInBytes,
            String contentType,
            Instant createdAt) {

        public static GetInboxFolderOutput.File from(org.osnormais.drive.api.domain.file.File file) {
            return new GetInboxFolderOutput.File(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getSize().bytes(),
                    file.getContent().type(),
                    file.getCreatedAt());
        }
    }

}
