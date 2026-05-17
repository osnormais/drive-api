package org.osnormais.drive.api.domain.transferchannel;

import static java.util.Objects.isNull;

import java.time.Duration;
import java.time.Instant;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.exception.TransferChannelExpiredException;
import org.osnormais.drive.api.domain.exception.TransferChannelNotOwnedByUserException;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkPermission;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class TransferChannel extends AggregateRoot<TransferChannelId> {

    private final TransferChannelType type;
    private final UserId user;
    private final FileId file;
    private final Instant expiresAt;
    private final ChunkSpecification chunkSpecification;

    private TransferChannel(
            final TransferChannelId id,
            final TransferChannelType type,
            final UserId user,
            final FileId file,
            final Instant expiresAt,
            final ChunkSpecification chunkSpecification) {
        super(id);
        this.type = type;
        this.user = user;
        this.file = file;
        this.expiresAt = expiresAt;
        this.chunkSpecification = chunkSpecification;
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(user))
            handler.append(ValidationError.with("TransferChannel.user is required"));
        else
            user.validate(handler);

        if (isNull(file))
            handler.append(ValidationError.with("TransferChannel.file is required"));
        else
            file.validate(handler);

        if (isNull(type))
            handler.append(ValidationError.with("TransferChannel.type is required"));

        if (isNull(chunkSpecification))
            handler.append(ValidationError.with("TransferChannel.chunkSpecification is required"));
        else
            chunkSpecification.validate(handler);

    }

    public static TransferChannel create(
            final Duration validDuration,
            final UserId user,
            final FileId file,
            final TransferChannelType type,
            final ChunkSpecification chunkSpecification) {

        final Instant expiresAt = Instant.now().plus(validDuration);

        return new TransferChannel(
                TransferChannelId.unique(),
                type,
                user,
                file,
                expiresAt,
                chunkSpecification);

    }

    public ChunkPermission allowChunk(final Long chunkIndex, final Duration validDuration) {

        final Instant now = Instant.now();

        if (expiresAt.isBefore(now))
            throw TransferChannelExpiredException.create();

        final Instant targetExpiresAt = now.plus(validDuration);
        if (this.expiresAt.isBefore(targetExpiresAt))
            return ChunkPermission.create(chunkIndex, type, expiresAt);

        return ChunkPermission.create(chunkIndex, type, targetExpiresAt);

    }

    public TransferChannel ensureBelongsTo(final UserId user) {
        if (!this.user.equals(user))
            throw TransferChannelNotOwnedByUserException.with(user);
        return this;
    }

    public TransferChannel ensureNotExpired() {
        if (expiresAt.isBefore(Instant.now()))
            throw TransferChannelExpiredException.create();
        return this;
    }

    public TransferChannelType getType() {
        return type;
    }

    public UserId getUser() {
        return user;
    }

    public FileId getFile() {
        return file;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public ChunkSpecification getChunkSpecification() {
        return chunkSpecification;
    }

}
