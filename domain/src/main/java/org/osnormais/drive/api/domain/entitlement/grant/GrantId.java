package org.osnormais.drive.api.domain.entitlement.grant;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class GrantId extends Identifier<UUID> {

    private GrantId(final UUID value) {
        super(value);
    }

    public static GrantId of(final UUID value) {
        return new GrantId(value);
    }

    public static GrantId unique() {
        return new GrantId(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

}
