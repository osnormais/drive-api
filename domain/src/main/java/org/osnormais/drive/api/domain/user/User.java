package org.osnormais.drive.api.domain.user;

import static java.util.Objects.isNull;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.user.valueobject.Quota;
import org.osnormais.drive.api.domain.user.valueobject.QuotaRequest;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class User extends AggregateRoot<UserId> implements DomainEventSource {

    private Quota quota;
    private Optional<QuotaRequest> quotaRequest;

    private final Queue<DomainEvent<?>> events;

    private User(
            final UserId id,
            final Quota quota,
            final Optional<QuotaRequest> quotaRequest,
            final Queue<DomainEvent<?>> events) {
        super(id);
        this.quota = quota;
        this.quotaRequest = quotaRequest;

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    @Override
    public Optional<DomainEvent<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public Quota getQuota() {
        return quota;
    }

    public Optional<QuotaRequest> getQuotaRequest() {
        return quotaRequest;
    }

}
