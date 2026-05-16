package org.osnormais.drive.api.domain.transferchannel;

import static java.util.Objects.isNull;

import java.time.Duration;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkPermission;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class TransferChannel extends AggregateRoot<TransferChannelId> {

    private final UserId user;
    private final FileId file;
    private final Type type;
    private final ChunkSpecification chunkSpecification;

    private TransferChannel(
            final TransferChannelId id,
            final UserId user,
            final FileId file,
            final Type type,
            final ChunkSpecification chunkSpecification) {
        super(id);
        this.user = user;
        this.file = file;
        this.type = type;
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

    public ChunkPermission allowChunk(final Long chunkIndex, final Duration validDuration) {
        return ChunkPermission.create(chunkIndex, type, validDuration);
    }

    public UserId getUser() {
        return user;
    }

    public FileId getFile() {
        return file;
    }

    public Type getType() {
        return type;
    }

    public ChunkSpecification getChunkSpecification() {
        return chunkSpecification;
    }

}
