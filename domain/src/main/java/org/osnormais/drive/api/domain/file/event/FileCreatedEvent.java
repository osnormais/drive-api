package org.osnormais.drive.api.domain.file.event;

import java.time.Instant;
import java.util.Set;

import org.osnormais.drive.api.domain.Entity;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventEntity;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;

public class FileCreatedEvent extends DomainEvent<FileId> {

    private static final Class<? extends Entity<?>> ENTITY_CLASS = File.class;
    private static final String ACTION = "created";

    private FileCreatedEvent(
            final File file,
            final Instant occurredAt,
            final Set<DomainEventEntity> relatedEntities) {
        super(
                file,
                null,
                ACTION,
                occurredAt,
                relatedEntities);
    }

    public static FileCreatedEvent create(final File file) {
        return new FileCreatedEvent(file, Instant.now(), Set.of(DomainEventEntity.of(file)));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, null, ACTION);
    }

}
