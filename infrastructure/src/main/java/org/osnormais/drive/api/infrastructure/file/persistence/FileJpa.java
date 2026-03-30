package org.osnormais.drive.api.infrastructure.file.persistence;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
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

@Entity(name = "File")
@Table(name = "files")
public class FileJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private UUID folderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Checksum.Algorithm checksumAlgorithm;

    @Column(nullable = false)
    private String checksumValue;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(nullable = false)
    private String name;

    private String contentType;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT) // TODO testar com e sem
    @CollectionTable(name = "file_sharings", joinColumns = @JoinColumn(name = "file_id"))
    private Set<FileSharingJpa> sharings;

    public FileJpa() {
    }

    private FileJpa(
            final UUID id,
            final UUID creatorId,
            final UUID ownerId,
            final UUID folderId,
            final Checksum.Algorithm checksumAlgorithm,
            final String checksumValue,
            final Long sizeInBytes,
            final String name,
            final String contentType,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Set<FileSharingJpa> sharings) {
        this.id = id;
        this.creatorId = creatorId;
        this.ownerId = ownerId;
        this.folderId = folderId;
        this.checksumAlgorithm = checksumAlgorithm;
        this.checksumValue = checksumValue;
        this.sizeInBytes = sizeInBytes;
        this.name = name;
        this.contentType = contentType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sharings = sharings;
    }

    public static FileJpa fromDomain(final File file) {

        return new FileJpa(
                file.getId().getValue(),
                file.getCreator().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getChecksum().algorithm(),
                file.getChecksum().value(),
                file.getSize().bytes(),
                file.getName().value(),
                file.getContent().type(),
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt(),
                file.getSharings().stream()
                        .map(FileSharingJpa::fromDomain)
                        .collect(Collectors.toSet()));

    }

    public File toDomain() {

        return File.with(
                FileId.of(getId()),
                UserId.of(getCreatorId()),
                UserId.of(getOwnerId()),
                FolderId.of(getFolderId()),
                FileName.of(getName()),
                Checksum.of(getChecksumAlgorithm(), getChecksumValue()),
                Size.of(getSizeInBytes()),
                Content.of(getContentType()),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt(),
                getSharings()
                        .stream()
                        .map(FileSharingJpa::toDomain)
                        .collect(Collectors.toSet()),
                null);

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

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }

    public Checksum.Algorithm getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(Checksum.Algorithm checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public String getChecksumValue() {
        return checksumValue;
    }

    public void setChecksumValue(String checksumValue) {
        this.checksumValue = checksumValue;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public Set<FileSharingJpa> getSharings() {
        return sharings;
    }

    public void setSharings(Set<FileSharingJpa> sharings) {
        this.sharings = sharings;
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
        FileJpa other = (FileJpa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}