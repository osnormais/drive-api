package org.osnormais.drive.api.infrastructure.messaging.producer;

@FunctionalInterface
public interface MessageProducer<T> {

    void produce(T payload);

}
