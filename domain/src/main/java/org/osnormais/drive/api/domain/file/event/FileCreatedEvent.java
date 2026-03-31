package org.osnormais.drive.api.domain.file.event;

import java.time.Instant;
import java.util.List;

import org.osnormais.drive.api.domain.Entity;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventEntity;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.user.User;

public class FileCreatedEvent extends DomainEvent<FileId> {

    private static final Class<? extends Entity<?>> ENTITY_CLASS = File.class;
    private static final String ACTION = "created";

    public FileCreatedEvent() {
    }

    private FileCreatedEvent(
            final File file,
            final Instant occurredAt,
            final List<DomainEventEntity> relatedEntities) {
        super(
                file,
                null,
                ACTION,
                occurredAt,
                relatedEntities);
    }

    public static FileCreatedEvent create(final File file) {
        return new FileCreatedEvent(
                file,
                Instant.now(),
                List.of(
                        DomainEventEntity.of(file),
                        DomainEventEntity.of(Folder.class, file.getFolder()),
                        DomainEventEntity.of(User.class, file.getOwner()),
                        DomainEventEntity.of(User.class, file.getCreator())));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, null, ACTION);
    }

}
