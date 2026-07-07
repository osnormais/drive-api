package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;
import org.osnormais.drive.api.domain.transferchannel.service.ChunkPermissionGenerationService;
import org.osnormais.drive.api.domain.transferchannel.service.ChunkPermissionGenerationService.ChunkPermission;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetTransferChannelPermissionUseCase extends GetTransferChannelPermissionUseCase {

    private final UserQueryGateway userQueryGateway;
    private final TransferChannelQueryGateway transferChannelQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final Duration validDuration;

    public DefaultGetTransferChannelPermissionUseCase(
            final UserQueryGateway userQueryGateway,
            final TransferChannelQueryGateway transferChannelQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final Long validDurationSeconds) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.transferChannelQueryGateway = requireNonNull(transferChannelQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.validDuration = Duration.ofSeconds(requireNonNull(validDurationSeconds));
    }

    @Override
    public GetTransferChannelPermissionOutput execute(final GetTransferChannelPermissionInput input) {

        final UserId actorId = UserId.of(input.actorId());
        final TransferChannelId transferChannelId = TransferChannelId.of(input.transferChannelId());

        if (!userQueryGateway.existsById(actorId))
            throw NotFoundException.create(User.class, actorId);

        final TransferChannel transferChannel = transferChannelQueryGateway
                .findById(transferChannelId)
                .orElseThrow(() -> NotFoundException.create(TransferChannel.class, transferChannelId));

        final File file = fileQueryGateway
                .findById(transferChannel.getFile())
                .orElseThrow(() -> NotFoundException.create(File.class, transferChannel.getFile()));

        final Set<Long> chunkIndexes = input
                .ranges()
                .stream()
                .flatMap(range -> LongStream
                        .rangeClosed(range.start(), range.end())
                        .boxed())
                .collect(Collectors.toSet());

        final ChunkPermission permission = ChunkPermissionGenerationService.generate(
                transferChannel,
                file,
                actorId,
                chunkIndexes,
                validDuration);

        return GetTransferChannelPermissionOutput.from(transferChannel, permission);
    }

}
