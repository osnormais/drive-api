package org.osnormais.drive.api.infrastructure.event.outbox;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class OutboxId extends Identifier<UUID> {

    protected OutboxId() {
    }

    public OutboxId(UUID id) {
        super(id);
    }

    public static OutboxId of(final UUID id) {
        return new OutboxId(id);
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

}
