package org.osnormais.drive.api.application.usecase.file.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.user.UserId;

public record ListFileOutput(Page<Item> page) {

    public record Item(
            UUID id,
            String name,
            Long sizeInBytes,
            String contentType,
            UUID folderId,
            UUID ownerId,
            UUID creatorId,
            Instant createdAt,
            Instant updatedAt) {

        public static Item from(final File file, final UserId actorId) {
            return new Item(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getSize().bytes(),
                    file.getContent().type(),
                    file.getFolderFor(actorId).getValue(),
                    file.getOwner().getValue(),
                    file.getCreator().getValue(),
                    file.getCreatedAt(),
                    file.getUpdatedAt());
        }

    }

}
