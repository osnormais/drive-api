package org.osnormais.drive.api.domain.event;

import java.util.UUID;

public abstract class DomainEventHandler<E extends DomainEvent<?>> {

    private final UUID id;
    private final String eventKey;

    protected DomainEventHandler(final UUID id, final String eventKey) {
        this.id = id;
        this.eventKey = eventKey;
    }

    public UUID id() {
        return id;
    }

    public String eventKey() {
        return eventKey;
    }

    public abstract void handle(E event);

}
