package org.osnormais.drive.api.domain.user;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class UserId extends Identifier<UUID> {

    private UserId(UUID id) {
        super(id);
    }

    public static UserId unique() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(final UUID id) {
        return new UserId(id);
    }

    @Override
    public String getStringValue() {
        return id.toString();
    }

}
