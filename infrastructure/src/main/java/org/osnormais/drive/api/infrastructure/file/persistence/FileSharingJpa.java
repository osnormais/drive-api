package org.osnormais.drive.api.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.FileSharing;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Embeddable;

@Embeddable
public class FileSharingJpa {

    private UUID sharedTo;
    private UUID sharedBy;
    private UUID virtualFolder;
    private Instant sharedAt;

    private FileSharingJpa(
            final UUID sharedTo,
            final UUID sharedBy,
            final UUID virtualFolder,
            final Instant sharedAt) {
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.sharedAt = sharedAt;
    }

    public static FileSharingJpa fromDomain(final FileSharing fileSharing) {
        return new FileSharingJpa(
                fileSharing.sharedTo().getValue(),
                fileSharing.sharedBy().getValue(),
                fileSharing.virtuaFolder().getValue(),
                fileSharing.sharedAt());
    }

    public FileSharing toDomain() {
        return FileSharing.with(
                UserId.of(getSharedTo()),
                UserId.of(getSharedBy()),
                FolderId.of(getVirtualFolder()),
                getSharedAt());
    }

    public FileSharingJpa() {
    }

    public UUID getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(UUID sharedTo) {
        this.sharedTo = sharedTo;
    }

    public UUID getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(UUID sharedBy) {
        this.sharedBy = sharedBy;
    }

    public UUID getVirtualFolder() {
        return virtualFolder;
    }

    public void setVirtualFolder(UUID virtualFolder) {
        this.virtualFolder = virtualFolder;
    }

    public Instant getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(Instant sharedAt) {
        this.sharedAt = sharedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sharedTo == null) ? 0 : sharedTo.hashCode());
        result = prime * result + ((sharedBy == null) ? 0 : sharedBy.hashCode());
        result = prime * result + ((virtualFolder == null) ? 0 : virtualFolder.hashCode());
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
        FileSharingJpa other = (FileSharingJpa) obj;
        if (sharedTo == null) {
            if (other.sharedTo != null)
                return false;
        } else if (!sharedTo.equals(other.sharedTo))
            return false;
        if (sharedBy == null) {
            if (other.sharedBy != null)
                return false;
        } else if (!sharedBy.equals(other.sharedBy))
            return false;
        if (virtualFolder == null) {
            if (other.virtualFolder != null)
                return false;
        } else if (!virtualFolder.equals(other.virtualFolder))
            return false;
        return true;
    }

}
