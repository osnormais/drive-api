package org.osnormais.drive.api.domain.folder.event;

import java.time.Instant;
import java.util.List;

import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventEntity;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class FolderSharedEvent extends DomainEvent<FolderId> {

    private static final Class<Folder> ENTITY_CLASS = Folder.class;
    private static final String ACTION = "shared";

    FolderSharedEvent() {
    }

    private FolderSharedEvent(
            final Folder folder,
            final Instant occurredAt,
            final List<DomainEventEntity> relatedEntities) {
        super(
                folder,
                null,
                ACTION,
                occurredAt,
                relatedEntities);
    }

    public static FolderSharedEvent create(
            final Folder folder,
            final UserId sharedWith,
            final UserId sharedBy,
            final FolderId virtualFolder) {
        return new FolderSharedEvent(
                folder,
                Instant.now(),
                List.of(
                        DomainEventEntity.of(folder),
                        DomainEventEntity.of(User.class, folder.getOwner()),
                        DomainEventEntity.of(User.class, sharedWith),
                        DomainEventEntity.of(User.class, sharedBy),
                        DomainEventEntity.of(Folder.class, virtualFolder)));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, null, ACTION);
    }
}
