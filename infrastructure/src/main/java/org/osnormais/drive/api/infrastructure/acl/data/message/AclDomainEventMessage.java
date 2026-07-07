package org.osnormais.drive.api.infrastructure.acl.data.message;

import java.io.Serializable;
import java.util.UUID;

public record AclDomainEventMessage(UUID aclId) implements Serializable {

}
