package org.osnormais.drive.api.domain.user;

import static java.util.Objects.isNull;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.entitlement.grant.GrantId;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class User extends AggregateRoot<UserId> implements DomainEventSource {

    private PlanId plan;
    private Optional<GrantId> activeGrant;

    private final Queue<DomainEvent<?>> events;

    private User(
            final UserId id,
            final PlanId plan,
            final GrantId activeGrant,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.plan = plan;
        this.activeGrant = Optional.ofNullable(activeGrant);

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();

    }

    @Override
    public void validate(ValidationHandler handler) {

        if (isNull(plan))
            handler.append(new ValidationError("'User.plan' cannot be null."));

        if (isNull(activeGrant))
            handler.append(new ValidationError("'User.activeGrant' cannot be null."));

    }

    public static User with(
            final UserId id,
            final PlanId plan,
            final GrantId activeGrant,
            final Queue<DomainEvent<?>> events) {
        return new User(id, plan, activeGrant, events);
    }

    @Override
    public Optional<DomainEvent<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    private void selfValidate() {
        final ValidationHandler notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw ValidationException.with("'User' validation failed", notification);
    }

    public PlanId getPlan() {
        return plan;
    }

    public Optional<GrantId> getActiveGrant() {
        return activeGrant;
    }

}
