package org.osnormais.drive.api.domain.entitlement.plan;

import org.osnormais.drive.api.domain.ValueObject;

public record PlanName(String value) implements ValueObject {

    public static PlanName of(final String value) {
        return new PlanName(value);
    }

}
