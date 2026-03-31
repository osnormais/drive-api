package org.osnormais.drive.api.infrastructure.event.outbox.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventContext;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.infrastructure.configuration.mapper.Mapper;
import org.osnormais.drive.api.infrastructure.event.outbox.gateway.OutboxJpaGateway;
import org.osnormais.drive.api.infrastructure.event.outbox.persistence.OutboxJpa;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OutboxEventDispatcher extends DomainEventDispatcher {

    private final ObjectMapper mapper = Mapper.mapper();
    private final OutboxJpaGateway outboxGateway;

    public OutboxEventDispatcher(final OutboxJpaGateway outboxGateway) {
        this.outboxGateway = requireNonNull(outboxGateway);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public <I extends Identifier<?>> DomainEventContext append(
            final DomainEventContext context,
            final DomainEvent<I> event) {

        handlerFor(event.key()).forEach(handler -> save(handler.id(), context, event));

        return context;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public <I extends Identifier<?>> DomainEventContext append(
            final DomainEventContext context,
            final DomainEventSource source) {

        var event = source.nextEvent();
        var nextContext = context;

        while (event.isPresent()) {

            final var actualEvent = event.get();
            final var actualContext = nextContext;

            handlerFor(actualEvent.key()).forEach(handler -> save(handler.id(), actualContext, actualEvent));

            event = source.nextEvent();
            nextContext = nextContext.createNext();

        }

        return nextContext;

    }

    @Override
    public void dispatch(DomainEventContext... contexts) {

        for (DomainEventContext context : contexts) {

            final var outBoxEvents = outboxGateway
                    .findByContextId(context.id())
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

    }

    private <I extends Identifier<?>> void save(
            final UUID handlerId,
            final DomainEventContext context,
            final DomainEvent<I> event) {

        final Map<String, Object> payload = mapper.convertValue(event, new TypeReference<>() {
        });

        outboxGateway.save(new OutboxJpa(
                context.id(),
                context.position(),
                event.key(),
                handlerId,
                payload));
    }

}
