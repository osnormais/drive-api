package org.osnormais.drive.api.infrastructure.entitlement.plan.gateway;

import java.util.Optional;

import org.osnormais.drive.api.application.gateway.entitlement.plan.PlanQueryGateway;
import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.infrastructure.entitlement.plan.persistence.PlanJpa;
import org.osnormais.drive.api.infrastructure.entitlement.plan.persistence.PlanJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PlanJpaGateway implements PlanQueryGateway {

    private final PlanJpaRepository planJpaRepository;

    public PlanJpaGateway(final PlanJpaRepository planJpaRepository) {
        this.planJpaRepository = planJpaRepository;
    }

    @Override
    public Optional<Plan> findById(final PlanId id) {
        return planJpaRepository
                .findById(id.getValue())
                .map(PlanJpa::toDomain);
    }

}
