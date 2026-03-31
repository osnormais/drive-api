package org.osnormais.drive.api.infrastructure.configuration.domain.event;

import java.util.List;

import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.infrastructure.event.outbox.dispatcher.OutboxEventDispatcher;
import org.osnormais.drive.api.infrastructure.event.outbox.gateway.OutboxJpaGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventDispatcherConfig {

    private final List<DomainEventHandler<?>> domainEventHandlers;
    private final OutboxJpaGateway outboxGateway;

    public DomainEventDispatcherConfig(
            final List<DomainEventHandler<?>> domainEventHandlers,
            final OutboxJpaGateway outboxGateway) {
        this.domainEventHandlers = domainEventHandlers;
        this.outboxGateway = outboxGateway;
    }

    @Bean
    DomainEventDispatcher eventDispatcher() {
        final OutboxEventDispatcher dispatcher = new OutboxEventDispatcher(outboxGateway);
        domainEventHandlers.forEach(handler -> dispatcher.register(handler.eventKey(), handler));
        return dispatcher;
    }

}
