package org.osnormais.drive.api.domain.acl;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class AclId extends Identifier<UUID> {

    public AclId(UUID value) {
        super(value);
    }

    public static AclId unique() {
        return AclId.of(UUID.randomUUID());
    }

    public static AclId of(final UUID id) {
        return new AclId(id);
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

}
