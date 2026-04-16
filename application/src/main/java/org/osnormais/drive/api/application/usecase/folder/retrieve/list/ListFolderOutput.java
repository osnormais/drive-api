package org.osnormais.drive.api.application.usecase.folder.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.user.UserId;

public record ListFolderOutput(Page<Item> page) {

    public record Item(
            UUID id,
            String name,
            FolderType type,
            UUID parentId,
            UUID ownerId,
            Instant createdAt,
            Instant updatedAt) {

        public static Item from(final Folder folder, final UserId actorId) {
            return new Item(
                    folder.getId().getValue(),
                    folder.getName().value(),
                    folder.getType(),
                    folder.getParentFolderFor(actorId).getValue(),
                    folder.getOwner().getValue(),
                    folder.getCreatedAt(),
                    folder.getUpdatedAt());
        }

    }

}
