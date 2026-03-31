package org.osnormais.drive.api.infrastructure.aop;

import static java.util.Objects.requireNonNull;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
@Order(value = 1)
public class TransactionalAspect {

    private final TransactionTemplate transactionTemplate;

    public TransactionalAspect(final TransactionTemplate transactionTemplate) {
        this.transactionTemplate = requireNonNull(transactionTemplate);
    }

    @Around("@annotation(org.osnormais.drive.api.application.common.annotation.Transactional)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return transactionTemplate.execute(new Transaction(joinPoint));
        } catch (final TransactionalAspect.Transaction.TransactionException e) {
            throw e.getCause();
        }
    }

    class Transaction implements TransactionCallback<Object> {

        private final ProceedingJoinPoint joinPoint;

        Transaction(final ProceedingJoinPoint joinPoint) {
            this.joinPoint = requireNonNull(joinPoint);
        }

        @Override
        public Object doInTransaction(TransactionStatus status) {
            try {
                final var result = joinPoint.proceed();
                return result;
            } catch (Throwable e) {
                status.setRollbackOnly();
                throw new TransactionException(e);
            }
        }

        static class TransactionException extends RuntimeException {

            TransactionException(Throwable cause) {
                super(cause);
            }

        }

    }

}
