package org.osnormais.drive.api.domain.transferchannel.valueobject;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.exception.DomainException;
import org.osnormais.drive.api.domain.exception.InvalidArgumentException;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record ChunkSpecification(
        ThroughputLimit throughputLimit,
        ChunkSize chunkSize,
        ParallelChunkLimit parallelChunkLimit) implements ValueObject {

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(throughputLimit))
            handler.append(ValidationError.with("ChunkSpecification.throughputLimit' should not be null"));
        else
            throughputLimit.validate(handler);

        if (isNull(chunkSize))
            handler.append(ValidationError.with("ChunkSpecification.chunkSize' should not be null"));
        else
            chunkSize.validate(handler);

        if (isNull(parallelChunkLimit))
            handler.append(ValidationError.with("ChunkSpecification.parallelChunkLimit' should not be null"));
        else
            parallelChunkLimit.validate(handler);

    }

    public ChunkSize effectiveChunkSize(final Long fileSizeInBytes) {
        return chunkSize.bytes() > fileSizeInBytes ? ChunkSize.of(fileSizeInBytes) : chunkSize;
    }

    public ChunkSize effectiveChunkSize(final Long fileSizeInBytes, final Long chunkIndex) {

        if (isNull(fileSizeInBytes))
            throw InvalidArgumentException
                    .with(DomainException.Error.with("'fileSize' should not be null"));

        if (isNull(chunkIndex))
            throw InvalidArgumentException
                    .with(DomainException.Error.with("'chunkIndex' should not be null"));

        final Long totalChunks = totalChunks(fileSizeInBytes);

        if (chunkIndex < 0 || chunkIndex >= totalChunks)
            throw InvalidArgumentException
                    .with(DomainException.Error
                            .with("'chunkIndex' out of bounds"));

        return (chunkIndex == totalChunks - 1)
                ? lastChunkSize(fileSizeInBytes)
                : effectiveChunkSize(fileSizeInBytes);

    }

    public ChunkSize lastChunkSize(final Long fileSizeInBytes) {

        final long hasPartialChunk = fileSizeInBytes % chunkSize.bytes() != 0 ? 1 : 0;
        final long lastChunkSize = hasPartialChunk == 1 ? fileSizeInBytes % chunkSize.bytes() : chunkSize.bytes();

        return ChunkSize.of(lastChunkSize);
    }

    public Long totalChunks(final Long fileSizeInBytes) {

        final long fullChunks = fileSizeInBytes / chunkSize.bytes();
        final long hasPartialChunk = fileSizeInBytes % chunkSize.bytes() != 0 ? 1 : 0;

        return fullChunks + hasPartialChunk;
    }

    public Long chunkOffset(final Long fileSizeInBytes, final Long chunkIndex) {

        if (isNull(fileSizeInBytes))
            throw InvalidArgumentException.with(DomainException.Error.with("'fileSize' should not be null"));

        if (isNull(chunkIndex))
            throw InvalidArgumentException.with(DomainException.Error.with("'chunkIndex' should not be null"));

        final Long totalChunks = totalChunks(fileSizeInBytes);

        if (chunkIndex < 0 || chunkIndex >= totalChunks)
            throw InvalidArgumentException.with(DomainException.Error.with("'chunkIndex' out of bounds"));

        return (chunkIndex == totalChunks - 1)
                ? fileSizeInBytes - lastChunkSize(fileSizeInBytes).bytes()
                : (chunkIndex * chunkSize.bytes());

    }

}
