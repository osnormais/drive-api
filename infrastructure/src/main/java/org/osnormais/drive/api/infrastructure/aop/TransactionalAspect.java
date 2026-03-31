package org.osnormais.drive.api.infrastructure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
@Order(value = 1)
public class TransactionalAspect {

    private final TransactionTemplate transactionTemplate;

    public TransactionalAspect(final TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Around("@annotation(org.osnormais.drive.api.application.common.annotation.Transactional)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return transactionTemplate.execute(status -> {
                try {

                    final Object result = joinPoint.proceed();

                    return result;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    throw new TransactionalAspectException(e);
                }
            });
        } catch (TransactionalAspectException e) {
            throw e.getCause();
        }
    }

    private static class TransactionalAspectException extends RuntimeException {
        public TransactionalAspectException(Throwable cause) {
            super(cause);
        }
    }

}
