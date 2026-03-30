package org.osnormais.drive.api.domain.folder;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class Folder extends AggregateRoot<FolderId> implements DomainEventSource {

    private final UserId creator;
    private final UserId owner;

    private Optional<FolderId> parentFolder;
    private FolderName name;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private final Queue<DomainEvent<?>> events;

    private Folder(
            final FolderId id,
            final UserId creator,
            final UserId owner,
            final FolderId parentFolder,
            final FolderName name,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.creator = creator;
        this.owner = owner;
        this.parentFolder = Optional.ofNullable(parentFolder);
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(creator))
            handler.append(new ValidationError("'Folder.creator' cannot be null."));
        else
            creator.validate(handler);

        if (isNull(owner))
            handler.append(new ValidationError("'Folder.owner' cannot be null."));
        else
            owner.validate(handler);

        if (isNull(parentFolder))
            handler.append(new ValidationError("'Folder.parentFolder' cannot be null."));
        else
            parentFolder.ifPresent(folderId -> folderId.validate(handler));

        if (isNull(name))
            handler.append(new ValidationError("'Folder.name' cannot be null."));
        else
            name.validate(handler);

    }

    public static Folder create(
            final UserId creator,
            final Folder parent,
            final FolderName name) {

        final Instant now = Instant.now();

        return new Folder(
                FolderId.unique(),
                creator,
                parent.getOwner(),
                parent.getId(),
                name,
                now,
                now,
                null,
                null);

    }

    public Boolean isRoot() {
        return parentFolder.isEmpty();
    }

    private void selfValidate() {
        final ValidationHandler notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw ValidationException.with("'Folder' validation failed", notification);
    }

    @Override
    public Optional<DomainEvent<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public UserId getCreator() {
        return creator;
    }

    public UserId getOwner() {
        return owner;
    }

    public Optional<FolderId> getParentFolder() {
        return parentFolder;
    }

    public FolderName getName() {
        return name;
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
