package org.osnormais.drive.api.infrastructure.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.osnormais.drive.api.domain.event.DomainEventContext;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Order(value = 0)
public class OutboxRelayAspect {

    private final DomainEventDispatcher dispatcher;

    public OutboxRelayAspect(final DomainEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @AfterReturning(pointcut = "execution(org.osnormais.drive.api.domain.event.DomainEventContext org.osnormais.drive.api.domain.event.DomainEventDispatcher.append(..))", returning = "result")
    public void captureResult(JoinPoint joinPoint, Object result) {

        if (result instanceof DomainEventContext context) {
            if (TransactionSynchronizationManager.isActualTransactionActive())
                TransactionSynchronizationManager.registerSynchronization(new PostCommitEventDispatcher(context));
            else
                dispatcher.dispatch(context);
        }

    }

    private class PostCommitEventDispatcher implements TransactionSynchronization {

        final DomainEventContext context;

        PostCommitEventDispatcher(final DomainEventContext context) {
            this.context = context;
        }

        @Override
        public void afterCommit() {
            dispatcher.dispatch(context);
        }

    }

}
