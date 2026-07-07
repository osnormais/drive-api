package org.osnormais.drive.api.application.gateway.entitlement.plan;

import java.util.Optional;

import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;

public interface PlanQueryGateway {

    Optional<Plan> findById(PlanId id);

}
