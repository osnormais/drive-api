package org.osnormais.drive.api.domain.file;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.FileSharing;
import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.file.valueobject.Publication;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class File extends AggregateRoot<FileId> implements DomainEventSource {

    private final UserId creator;
    private final UserId owner;

    private FolderId folder;

    private final Checksum checksum;
    private final FileSize size;
    private FileName name;
    private Content content;
    private Optional<Publication> publication;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Set<FileSharing> sharings;

    private final Queue<DomainEvent<?>> events;

    private File(
            final FileId id,
            final UserId creator,
            final UserId owner,
            final FolderId folder,
            final FileName name,
            final Checksum checksum,
            final FileSize size,
            final Content content,
            final Optional<Publication> publication,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Set<FileSharing> sharings,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.creator = creator;
        this.owner = owner;
        this.folder = folder;
        this.name = name;
        this.checksum = checksum;
        this.size = size;
        this.content = content;
        this.publication = publication;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sharings = isNull(sharings) ? new HashSet<>() : new HashSet<>(sharings);

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();
    }

    public static File with(
            final FileId id,
            final UserId creator,
            final UserId owner,
            final FolderId folder,
            final FileName name,
            final Checksum checksum,
            final FileSize size,
            final Content content,
            final Publication publication,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Set<FileSharing> sharings,
            final Queue<DomainEvent<?>> events) {
        return new File(
                id,
                creator,
                owner,
                folder,
                name,
                checksum,
                size,
                content,
                Optional.ofNullable(publication),
                createdAt,
                updatedAt,
                deletedAt,
                sharings,
                events);
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

        if (isNull(createdAt))
            handler.append(new ValidationError("'File.createdAt' should not be null"));

        if (isNull(updatedAt))
            handler.append(new ValidationError("'File.updatedAt' should not be null"));

        if (isNull(publication))
            handler.append(new ValidationError("'File.publication' should not be null"));
        else
            publication.ifPresent(p -> p.validate(handler));

    }

    public static File create(
            final UserId creator,
            final UserId owner,
            final FolderId folder,
            final FileName name,
            final Checksum checksum,
            final FileSize size,
            final Content content) {

        final Instant now = Instant.now();

        final File file = new File(
                FileId.unique(),
                creator,
                owner,
                folder,
                name,
                checksum,
                size,
                content,
                Optional.empty(),
                now,
                now,
                null,
                null,
                null);

        file.events.add(FileCreatedEvent.create(file));

        return file;

    }

    public FolderId getFolderFor(final UserId user) {

        if (owner.equals(user))
            return folder;

        return sharings
                .stream()
                .filter(sharing -> sharing.sharedTo().equals(user))
                .findFirst()
                .map(FileSharing::virtualFolder)
                .orElse(folder);

    }

    public Boolean isPublished() {
        return this.publication
                .map(Publication::status)
                .filter(status -> Publication.Status.SUCCESS.equals(status))
                .isPresent();
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

    public UserId getCreator() {
        return creator;
    }

    public UserId getOwner() {
        return owner;
    }

    public FolderId getFolder() {
        return folder;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public FileSize getSize() {
        return size;
    }

    public FileName getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public Optional<Publication> getPublication() {
        return publication;
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

    public Set<FileSharing> getSharings() {
        return Set.copyOf(sharings);
    }

}
