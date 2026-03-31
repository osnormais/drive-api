package org.osnormais.drive.api.infrastructure.event.outbox.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osnormais.drive.api.domain.Identifier;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventContext;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.infrastructure.configuration.mapper.Mapper;
import org.osnormais.drive.api.infrastructure.event.outbox.gateway.OutboxJpaGateway;
import org.osnormais.drive.api.infrastructure.event.outbox.persistence.OutboxJpa;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OutboxEventDispatcher implements DomainEventDispatcher {

    private final ConcurrentHashMap<String, List<DomainEventHandler<?>>> handlers = new ConcurrentHashMap<>();

    private final ObjectMapper mapper = Mapper.mapper();
    private final OutboxJpaGateway outboxGateway;

    public OutboxEventDispatcher(final OutboxJpaGateway outboxGateway) {
        this.outboxGateway = requireNonNull(outboxGateway);
    }

    @Override
    public void register(final String eventKey, final DomainEventHandler<?> handler) {
        this.handlers.computeIfAbsent(eventKey, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    @Override
    public void unregister(final String eventKey, final DomainEventHandler<?> handler) {
        this.handlers.computeIfPresent(eventKey, (k, v) -> {
            v.remove(handler);
            return v.isEmpty() ? null : v;
        });
    }

    @Override
    public void unregisterAll(final String eventKey) {
        this.handlers.remove(eventKey);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public <I extends Identifier<?>> void notify(final DomainEventContext context, final DomainEvent<I> event) {
        handlerFor(event.key())
                .forEach(handler -> outboxGateway.save(toJpa(handler.id(), context, event)));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public <I extends Identifier<?>> void notify(final DomainEventContext context, final DomainEventSource source) {
        var event = source.nextEvent();
        var nextContext = context;
        while (event.isPresent()) {

            final var actualEvent = event.get();
            final var actualContext = nextContext;

            handlerFor(actualEvent.key())
                    .forEach(handler -> outboxGateway.save(toJpa(handler.id(), actualContext, actualEvent)));

            event = source.nextEvent();
            nextContext = nextContext.createNext();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void execute(final DomainEventContext context) {
        final var outBoxEvents = outboxGateway.findByContextId(context.id())
                .stream()
                .sorted(Comparator.comparing(OutboxJpa::getContextPosition));

        outBoxEvents.forEach(
                event -> {
                    // var handler = handlerFor(event);
                    // if (handler.isEmpty()) {
                    // return;
                    // }
                    // handler.get().handle(event);
                    outboxGateway.delete(event.getId());
                });

    }

    private List<DomainEventHandler<?>> handlerFor(final String eventKey) {
        return this.handlers.getOrDefault(eventKey, List.of());
    }

    private <I extends Identifier<?>> OutboxJpa toJpa(
            final UUID handlerId,
            final DomainEventContext context,
            final DomainEvent<I> event) {

        final Map<String, Object> payload = mapper.convertValue(event, new TypeReference<>() {
        });

        return new OutboxJpa(
                context.id(),
                context.position(),
                event.key(),
                handlerId,
                payload);
    }

}
