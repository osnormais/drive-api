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

public class FilePublicationInitiedEvent extends DomainEvent<FileId> {

    private static final Class<? extends Entity<?>> ENTITY_CLASS = File.class;
    private static final String SUB_RESOURCE = "publication";
    private static final String ACTION = "initiated";

    public FilePublicationInitiedEvent() {
    }

    private FilePublicationInitiedEvent(
            final File file,
            final Instant occurredAt,
            final List<DomainEventEntity> relatedEntities) {
        super(
                file,
                SUB_RESOURCE,
                ACTION,
                occurredAt,
                relatedEntities);
    }

    public static FilePublicationInitiedEvent create(final File file) {
        return new FilePublicationInitiedEvent(
                file,
                Instant.now(),
                List.of(
                        DomainEventEntity.of(file),
                        DomainEventEntity.of(Folder.class, file.getFolder()),
                        DomainEventEntity.of(User.class, file.getOwner()),
                        DomainEventEntity.of(User.class, file.getCreator())));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, SUB_RESOURCE, ACTION);
    }

}
