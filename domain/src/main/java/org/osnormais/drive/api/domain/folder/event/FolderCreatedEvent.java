package org.osnormais.drive.api.domain.folder.event;

import java.time.Instant;
import java.util.List;

import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventEntity;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.User;

public class FolderCreatedEvent extends DomainEvent<FolderId> {

    private static final Class<Folder> ENTITY_CLASS = Folder.class;
    private static final String ACTION = "created";

    private FolderCreatedEvent(
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

    public static FolderCreatedEvent create(final Folder folder) {
        return new FolderCreatedEvent(
                folder,
                Instant.now(),
                List.of(
                        DomainEventEntity.of(folder),
                        DomainEventEntity.of(User.class, folder.getOwner()),
                        DomainEventEntity.of(User.class, folder.getCreator())));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, null, ACTION);
    }

}
