package org.osnormais.drive.api.domain.entitlement.plan;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class PlanId extends Identifier<UUID> {

    private PlanId(final UUID value) {
        super(value);
    }

    public static PlanId of(final UUID value) {
        return new PlanId(value);
    }

    public static PlanId unique() {
        return new PlanId(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

}
