package org.osnormais.drive.api.infrastructure.event.outbox.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<OutboxJpa, UUID> {

    List<OutboxJpa> findByContextId(UUID batch_id);

}
