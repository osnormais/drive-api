package org.osnormais.drive.api.infrastructure.acl.persistence;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclEntry;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Embeddable;

@Embeddable
public class AclEntryJpa {

    private UUID userId;
    private Permission permission;
    private Instant grantedAt;
    private Instant expiresAt;
    private Boolean hasExpiration;

    public AclEntryJpa() {
    }

    private AclEntryJpa(
            final UUID userId,
            final Permission permission,
            final Instant grantedAt,
            final Instant expiresAt,
            final Boolean hasExpiration) {
        this.userId = userId;
        this.permission = permission;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.hasExpiration = hasExpiration;
    }

    public static AclEntryJpa fromDomain(final AclEntry aclEntry) {
        return new AclEntryJpa(
                aclEntry.user().getValue(),
                aclEntry.permission(),
                aclEntry.grantedAt(),
                aclEntry.expiresAt().orElse(null),
                aclEntry.expiresAt().isPresent());
    }

    public AclEntry toDomain() {
        return new AclEntry(
                UserId.of(getUserId()),
                getPermission(),
                getGrantedAt(),
                Optional.ofNullable(getExpiresAt()));
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Instant grantedAt) {
        this.grantedAt = grantedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getHasExpiration() {
        return hasExpiration;
    }

    public void setHasExpiration(Boolean hasExpiration) {
        this.hasExpiration = hasExpiration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((permission == null) ? 0 : permission.hashCode());
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
        AclEntryJpa other = (AclEntryJpa) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (permission != other.permission)
            return false;
        return true;
    }

}
