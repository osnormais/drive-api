package org.osnormais.drive.api.infrastructure.transferchannel.persistence;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSize;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ParallelChunkLimit;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ThroughputLimit;
import org.osnormais.drive.api.domain.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "TransferChannel")
@Table(name = "transfer_channels")
public class TransferChannelJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferChannelType type;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID fileId;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Long chunkSpecThroughputLimitBps;

    @Column(nullable = false)
    private Long chunkSpecChunkSizeBytes;

    @Column(nullable = false)
    private Integer chunkSpecParallelChunkLimit;

    public TransferChannelJpa() {
    }

    private TransferChannelJpa(
            final UUID id,
            final TransferChannelType type,
            final UUID userId,
            final UUID fileId,
            final Instant expiresAt,
            final Long chunkSpecThroughputLimitBps,
            final Long chunkSpecChunkSizeBytes,
            final Integer chunkSpecParallelChunkLimit) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.fileId = fileId;
        this.expiresAt = expiresAt;
        this.chunkSpecThroughputLimitBps = chunkSpecThroughputLimitBps;
        this.chunkSpecChunkSizeBytes = chunkSpecChunkSizeBytes;
        this.chunkSpecParallelChunkLimit = chunkSpecParallelChunkLimit;
    }

    public static TransferChannelJpa fromDomain(final TransferChannel transferChannel) {

        return new TransferChannelJpa(
                transferChannel.getId().getValue(),
                transferChannel.getType(),
                transferChannel.getUser().getValue(),
                transferChannel.getFile().getValue(),
                transferChannel.getExpiresAt(),
                transferChannel.getChunkSpecification().throughputLimit().bytesPerSecond(),
                transferChannel.getChunkSpecification().chunkSize().bytes(),
                transferChannel.getChunkSpecification().parallelChunkLimit().value());

    }

    public TransferChannel toDomain() {

        return TransferChannel.with(
                TransferChannelId.of(id),
                type,
                UserId.of(userId),
                FileId.of(fileId),
                expiresAt,
                new ChunkSpecification(
                        ThroughputLimit.of(chunkSpecThroughputLimitBps),
                        ChunkSize.of(chunkSpecChunkSizeBytes),
                        ParallelChunkLimit.of(chunkSpecParallelChunkLimit)));

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransferChannelType getType() {
        return type;
    }

    public void setType(TransferChannelType type) {
        this.type = type;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getChunkSpecThroughputLimitBps() {
        return chunkSpecThroughputLimitBps;
    }

    public void setChunkSpecThroughputLimitBps(Long chunkSpecThroughputLimitBps) {
        this.chunkSpecThroughputLimitBps = chunkSpecThroughputLimitBps;
    }

    public Long getChunkSpecChunkSizeBytes() {
        return chunkSpecChunkSizeBytes;
    }

    public void setChunkSpecChunkSizeBytes(Long chunkSpecChunkSizeBytes) {
        this.chunkSpecChunkSizeBytes = chunkSpecChunkSizeBytes;
    }

    public Integer getChunkSpecParallelChunkLimit() {
        return chunkSpecParallelChunkLimit;
    }

    public void setChunkSpecParallelChunkLimit(Integer chunkSpecParallelChunkLimit) {
        this.chunkSpecParallelChunkLimit = chunkSpecParallelChunkLimit;
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
        TransferChannelJpa other = (TransferChannelJpa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
