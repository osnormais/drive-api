package org.osnormais.drive.api.infrastructure.acl.persistence;

import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.osnormais.drive.api.domain.acl.valueobject.AclEntry;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "Acl")
@Table(name = "acls")
public class AclJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AclResourceType resourceType;

    @Column(nullable = false)
    private UUID resourceOwnerId;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "aclId", fetch = FetchType.LAZY)
    private Set<AclEntryJpa> entries;

    public AclJpa() {
    }

    private AclJpa(
            final UUID id,
            final UUID resourceId,
            final AclResourceType resourceType,
            final UUID resourceOwnerId,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resourceOwnerId = resourceOwnerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.entries = new HashSet<>();
    }

    public static AclJpa fromDomain(final Acl acl) {

        final AclJpa aclJpa = new AclJpa(
                acl.getId().getValue(),
                acl.getResource().resourceId().getValue(),
                acl.getResource().resourceType(),
                acl.getResource().owner().getValue(),
                acl.getCreatedAt(),
                acl.getUpdatedAt());

        final Set<AclEntryJpa> directEntries = nonNull(acl.getDirectEntries()) ? acl.getDirectEntries()
                .stream()
                .map(entry -> AclEntryJpa.fromDomain(aclJpa, AclEntryType.DIRECT, entry))
                .collect(Collectors.toSet()) : new HashSet<>();

        final Set<AclEntryJpa> inheritedEntries = nonNull(acl.getInheritedEntries()) ? acl.getInheritedEntries()
                .stream()
                .map(entry -> AclEntryJpa.fromDomain(aclJpa, AclEntryType.INHERITED, entry))
                .collect(Collectors.toSet()) : new HashSet<>();

        aclJpa.setEntries(directEntries, inheritedEntries);

        return aclJpa;

    }

    public Acl toDomain(
            final Set<AclEntry> directEntries,
            final Set<AclEntry> inheritedEntries) {

        final AclResource<?> resource = switch (getResourceType()) {
            case FILE -> new AclResource<>(
                    FileId.of(getResourceId()),
                    getResourceType(),
                    UserId.of(getResourceOwnerId()));

            case FOLDER -> new AclResource<>(
                    FolderId.of(getResourceId()),
                    getResourceType(),
                    UserId.of(getResourceOwnerId()));

            default -> throw new IllegalArgumentException("Tipo de recurso desconhecido: " + getResourceType());
        };

        return Acl.with(
                AclId.of(getId()),
                resource,
                directEntries,
                inheritedEntries,
                createdAt,
                updatedAt,
                null);

    }

    private void setEntries(Set<AclEntryJpa> directEntries, Set<AclEntryJpa> inheritedEntries) {
        this.entries = new HashSet<>();
        this.entries.addAll(directEntries);
        this.entries.addAll(inheritedEntries);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public AclResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(AclResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public UUID getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(UUID resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<AclEntryJpa> getEntries() {
        return entries;
    }

    public void setEntries(Set<AclEntryJpa> entries) {
        this.entries = entries;
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
        AclJpa other = (AclJpa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
