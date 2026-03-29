package org.osnormais.drive.api.domain.file;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class File extends AggregateRoot<FileId> implements DomainEventSource {

    private final Checksum checksum;
    private final Size size;
    private FileName name;
    private Content content;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private final Queue<DomainEvent<?>> events;

    private File(
            final FileId id,
            final FileName name,
            final Checksum checksum,
            final Size size,
            final Content content,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.name = name;
        this.checksum = checksum;
        this.size = size;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();
    }

    public static File create(
            final FileName name,
            final Checksum checksum,
            final Size size,
            final Content content) {

        final Instant now = Instant.now();

        final File file = new File(
                FileId.unique(),
                name,
                checksum,
                size,
                content,
                now,
                now,
                null,
                null);

        file.events.add(FileCreatedEvent.create(file));

        return file;

    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(checksum))
            handler.append(new ValidationError("'File.checksum' should not be null"));
        else
            checksum.validate(handler);

        if (isNull(size))
            handler.append(new ValidationError("'File.size' should not be null"));
        else
            size.validate(handler);

        if (isNull(name))
            handler.append(new ValidationError("'File.name' should not be null"));
        else
            name.validate(handler);

        if (isNull(content))
            handler.append(new ValidationError("'File.content' should not be null"));
        else
            content.validate(handler);

    }

    @Override
    public Optional<DomainEvent<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    private void selfValidate() {
        final ValidationHandler notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw ValidationException.with("'File' validation failed", notification);
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public Size getSize() {
        return size;
    }

    public FileName getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

}
