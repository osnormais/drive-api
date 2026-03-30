package org.osnormais.drive.api.domain.acl;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.acl.event.AclCreatedEvent;
import org.osnormais.drive.api.domain.acl.valueobject.AclEntry;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class Acl extends AggregateRoot<AclId> implements DomainEventSource {

    private final AclResource<?> resource;
    private Set<AclEntry> directEntries;
    private Set<AclEntry> inheritedEntries;

    private final Instant createdAt;
    private Instant updatedAt;

    private final Queue<DomainEvent<?>> events;

    private Acl(
            final AclId id,
            final AclResource<?> resource,
            final Set<AclEntry> directEntries,
            final Set<AclEntry> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.resource = resource;
        this.directEntries = isNull(events) ? new HashSet<>() : new HashSet<>(directEntries);
        this.inheritedEntries = isNull(events) ? new HashSet<>() : new HashSet<>(inheritedEntries);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();

    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(resource))
            handler.append(new ValidationError("'Acl.resource' cannot be null."));
        else
            resource.validate(handler);

        if (isNull(directEntries))
            handler.append(new ValidationError("'Acl.directEntries' cannot be null."));
        else
            directEntries.forEach(entry -> entry.validate(handler));

        if (isNull(inheritedEntries))
            handler.append(new ValidationError("'Acl.inheritedEntries' cannot be null."));
        else
            inheritedEntries.forEach(entry -> entry.validate(handler));

        if (isNull(createdAt))
            handler.append(new ValidationError("'Acl.createdAt' cannot be null."));

        if (isNull(updatedAt))
            handler.append(new ValidationError("'Acl.updatedAt' cannot be null."));

    }

    public static Acl create(final AclResource<?> resource) {

        final Instant now = Instant.now();

        final Acl acl = new Acl(
                AclId.unique(),
                resource,
                null,
                null,
                now,
                now,
                null);

        acl.events.add(AclCreatedEvent.create(acl));

        return acl;
    }

    public Acl requiredPermission(final UserId user, final Permission permission) {

        if (resource.owner().equals(user))
            return this;

        final Boolean hasPermission = Stream
                .concat(directEntries.stream(), inheritedEntries.stream())
                .anyMatch(entry -> entry.user().equals(user) && entry.permission().includes(permission));

        if (!hasPermission)

            

            throw new SecurityException(
                    "User [%s] does not have required permission [%s] for resource [%s]".formatted(
                            user,
                            permission,
                            resource));

        // {...}
        return this;
    }

    public Acl deriveFor(final AclResource<?> resource) {
        return create(resource).inheritFrom(this);
    }

    @Override
    public Optional<DomainEvent<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    private Acl inheritFrom(final Acl parentAcl) {
        this.inheritedEntries = parentAcl.inheritEntries();
        this.updatedAt = Instant.now();
        return this;
    }

    private Set<AclEntry> inheritEntries() {
        return Stream
                .concat(directEntries.stream(), inheritedEntries.stream())
                .collect(Collectors.toSet());
    }

    private void selfValidate() {
        final ValidationHandler notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw ValidationException.with("'Acl' validation failed", notification);
    }

    public AclResource<?> getResource() {
        return resource;
    }

    public Set<AclEntry> getDirectEntries() {
        return directEntries;
    }

    public Set<AclEntry> getInheritedEntries() {
        return inheritedEntries;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
