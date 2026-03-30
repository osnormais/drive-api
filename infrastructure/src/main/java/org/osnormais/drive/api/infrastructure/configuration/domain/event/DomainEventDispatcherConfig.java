package org.osnormais.drive.api.infrastructure.configuration.domain.event;

import java.util.List;

import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventDispatcherConfig {

    @Bean
    DomainEventDispatcher eventDispatcher(final List<DomainEventHandler<?>> eventHandlers) {
        final var dispatcher = new DomainEventDispatcher();
        eventHandlers.forEach(handler -> dispatcher.register(handler.eventKey(), handler));
        return dispatcher;
    }

}
