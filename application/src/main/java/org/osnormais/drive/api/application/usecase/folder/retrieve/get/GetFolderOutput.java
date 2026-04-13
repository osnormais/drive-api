package org.osnormais.drive.api.application.usecase.folder.retrieve.get;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.user.UserId;

public record GetFolderOutput(
        UUID id,
        String name,
        FolderType type,
        UUID parentId,
        List<GetFolderOutput.SubFolder> subFolders,
        List<GetFolderOutput.File> files,
        UUID ownerId,
        Instant createdAt,
        Instant updatedAt) {

    public static GetFolderOutput from(
            final UserId userId,
            final Folder folder,
            final Set<Folder> subFolders,
            final Set<org.osnormais.drive.api.domain.file.File> files) {

        return new GetFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getType(),
                folder.getParentFolderFor(userId).getValue(),
                subFolders.stream().map(GetFolderOutput.SubFolder::from).toList(),
                files.stream().map(GetFolderOutput.File::from).toList(),
                folder.getOwner().getValue(),
                folder.getCreatedAt(),
                folder.getUpdatedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetFolderOutput.SubFolder from(final Folder subFolder) {
            return new SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
        }

    }

    public static record File(
            UUID id,
            String name,
            Long sizeInBytes,
            String contentType,
            Instant createdAt) {

        public static GetFolderOutput.File from(org.osnormais.drive.api.domain.file.File file) {
            return new GetFolderOutput.File(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getSize().bytes(),
                    file.getContent().type(),
                    file.getCreatedAt());
        }
    }

}
