package org.osnormais.drive.api.infrastructure.acl.persistence;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclEntry;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "AclEntry")
@Table(name = "acl_entries")
public class AclEntryJpa {

    @EmbeddedId
    private AclEntryIdJpa id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acl_id", insertable = false, updatable = false)
    private AclJpa acl;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private AclEntryType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @Column(nullable = false)
    private Instant grantedAt;

    private Instant expiresAt;

    @Column(nullable = false)
    private Boolean hasExpiration;

    public AclEntryJpa(
            final AclEntryIdJpa id,
            final AclJpa acl,
            final UUID userId,
            final AclEntryType type,
            final Permission permission,
            final Instant grantedAt,
            final Instant expiresAt,
            final Boolean hasExpiration) {
        this.id = id;
        this.acl = acl;
        this.userId = userId;
        this.type = type;
        this.permission = permission;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.hasExpiration = hasExpiration;
    }

    public AclEntryJpa() {
    }

    public static AclEntryJpa fromDomain(final AclJpa acl, final AclEntryType type, final AclEntry aclEntry) {

        final AclEntryIdJpa id = new AclEntryIdJpa(
                acl.getId(),
                aclEntry.user().getValue(),
                type);

        return new AclEntryJpa(
                id,
                acl,
                aclEntry.user().getValue(),
                type,
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

    public AclEntryIdJpa getId() {
        return id;
    }

    public void setId(AclEntryIdJpa id) {
        this.id = id;
    }

    public AclJpa getAcl() {
        return acl;
    }

    public void setAcl(AclJpa acl) {
        this.acl = acl;
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
