package org.osnormais.drive.api.infrastructure.folder.persistence;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity(name = "Folder")
@Table(name = "folders")
public class FolderJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FolderType type;

    private UUID parentFolderId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT) // TODO testar com e sem
    @CollectionTable(name = "folder_sharings", joinColumns = @JoinColumn(name = "folder_id"))
    private Set<FolderSharingJpa> sharings;

    public FolderJpa(
            final UUID id,
            final UUID creatorId,
            final UUID ownerId,
            final FolderType type,
            final UUID parentFolderId,
            final String name,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Set<FolderSharingJpa> sharings) {
        this.id = id;
        this.creatorId = creatorId;
        this.ownerId = ownerId;
        this.parentFolderId = parentFolderId;
        this.type = type;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sharings = sharings;
    }

    public Folder toDomain() {

        return Folder.with(
                FolderId.of(getId()),
                UserId.of(getCreatorId()),
                UserId.of(getOwnerId()),
                getType(),
                isNull(getParentFolderId()) ? null : FolderId.of(getParentFolderId()),
                FolderName.of(getName()),
                createdAt,
                updatedAt,
                deletedAt,
                getSharings().stream().map(FolderSharingJpa::toDomain).collect(Collectors.toSet()),
                null);
    }

    public static FolderJpa fromDomain(final Folder folder) {
        return new FolderJpa(
                folder.getId().getValue(),
                folder.getCreator().getValue(),
                folder.getOwner().getValue(),
                folder.getType(),
                folder.getParentFolder().map(FolderId::getValue).orElse(null),
                folder.getName().value(),
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt(),
                folder.getSharings().stream().map(FolderSharingJpa::fromDomain).collect(Collectors.toSet()));
    }

    public FolderJpa() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(UUID parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public FolderType getType() {
        return type;
    }

    public void setType(FolderType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<FolderSharingJpa> getSharings() {
        return sharings;
    }

    public void setSharings(Set<FolderSharingJpa> sharings) {
        this.sharings = sharings;
    }

}
