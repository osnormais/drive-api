package org.osnormais.drive.api.domain.event;

import org.osnormais.drive.api.domain.Identifier;

public interface DomainEventDispatcher {

    void register(final String eventKey, final DomainEventHandler<?> handler);

    void unregister(final String eventKey, final DomainEventHandler<?> handler);

    void unregisterAll(final String eventKey);

    <I extends Identifier<?>> void notify(final DomainEventContext context, final DomainEvent<I> event);

    <I extends Identifier<?>> void notify(final DomainEventContext context, final DomainEventSource source);

    default void notify(final DomainEventSource source) {
        notify(DomainEventContext.create(), source);
    }

}
