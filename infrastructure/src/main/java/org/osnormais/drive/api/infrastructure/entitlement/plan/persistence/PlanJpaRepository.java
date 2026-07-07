package org.osnormais.drive.api.infrastructure.entitlement.plan.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanJpaRepository extends JpaRepository<PlanJpa, UUID> {

}
