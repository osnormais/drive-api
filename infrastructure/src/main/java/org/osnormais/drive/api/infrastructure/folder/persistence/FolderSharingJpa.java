package org.osnormais.drive.api.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.valueobject.FolderSharing;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Embeddable;

@Embeddable
public class FolderSharingJpa {

    private UUID sharedTo;
    private UUID sharedBy;
    private UUID virtualFolder;
    private Instant sharedAt;

    private FolderSharingJpa(
            final UUID sharedTo,
            final UUID sharedBy,
            final UUID virtualFolder,
            final Instant sharedAt) {
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.sharedAt = sharedAt;
    }

    public static FolderSharingJpa fromDomain(final FolderSharing folderSharing) {
        return new FolderSharingJpa(
                folderSharing.sharedTo().getValue(),
                folderSharing.sharedBy().getValue(),
                folderSharing.virtualFolder().getValue(),
                folderSharing.sharedAt());
    }

    public FolderSharing toDomain() {
        return FolderSharing.with(
                UserId.of(getSharedTo()),
                UserId.of(getSharedBy()),
                FolderId.of(getVirtualFolder()),
                getSharedAt());
    }

    public FolderSharingJpa() {
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

}
