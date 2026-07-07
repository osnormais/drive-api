package org.osnormais.drive.api.infrastructure.acl.persistence;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class AclEntryIdJpa {

    @Column(name = "acl_id", nullable = false)
    private UUID aclId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AclEntryType type;

    public AclEntryIdJpa() {
    }

    public AclEntryIdJpa(UUID aclId, UUID userId, AclEntryType type) {
        this.aclId = aclId;
        this.userId = userId;
        this.type = type;
    }

    public UUID getAclId() {
        return aclId;
    }

    public void setAclId(UUID aclId) {
        this.aclId = aclId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public AclEntryType getType() {
        return type;
    }

    public void setType(AclEntryType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aclId == null) ? 0 : aclId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AclEntryIdJpa other = (AclEntryIdJpa) obj;
        if (aclId == null) {
            if (other.aclId != null)
                return false;
        } else if (!aclId.equals(other.aclId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
