package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelInput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission.GetTransferChannelPermissionInput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission.GetTransferChannelPermissionInput.Range;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission.GetTransferChannelPermissionOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission.GetTransferChannelPermissionUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelInput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelUseCase;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.infrastructure.api.TransferChannelAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.ChunkToken;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelResponse;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelTokensResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferChannelController implements TransferChannelAPI {

    private final CreateTransferChannelUseCase createTransferChannelUseCase;
    private final GetTransferChannelUseCase getTransferChannelUseCase;
    private final GetTransferChannelPermissionUseCase getTransferChannelPermissionUseCase;

    public TransferChannelController(
            final CreateTransferChannelUseCase createTransferChannelUseCase,
            final GetTransferChannelUseCase getTransferChannelUseCase,
            final GetTransferChannelPermissionUseCase getTransferChannelPermissionUseCase) {
        this.createTransferChannelUseCase = requireNonNull(createTransferChannelUseCase);
        this.getTransferChannelUseCase = requireNonNull(getTransferChannelUseCase);
        this.getTransferChannelPermissionUseCase = requireNonNull(getTransferChannelPermissionUseCase);
    }

    @Override
    public ResponseEntity<GetTransferChannelResponse> createTrasnferChannel(
            final UUID fileId,
            final TransferChannelType type) {

        final CreateTransferChannelOutput output = createTransferChannelUseCase.execute(
                new CreateTransferChannelInput(SecurityContext.getAuthenticatedUserId(), fileId, type));

        return ResponseEntity.ok(new GetTransferChannelResponse(output.id(), output.totalChunks(), output.expiresAt()));

    }

    @Override
    public ResponseEntity<GetTransferChannelTokensResponse> getTransferChannelChunksPermissionTokens(
            final UUID id,
            final String ranges) {

        final Set<Range> rangeSet = List.of(ranges.split(";"))
                .stream()
                .map(range -> range.split("-"))
                .map(range -> new Range(Long.parseLong(range[0]), Long.parseLong(range[1])))
                .collect(Collectors.toSet());

        final GetTransferChannelPermissionOutput output = getTransferChannelPermissionUseCase
                .execute(new GetTransferChannelPermissionInput(SecurityContext.getAuthenticatedUserId(), id, rangeSet));

        final Set<ChunkToken> chunkTokens = output.chunks()
                .stream()
                .map(chunkInfo -> new ChunkToken(chunkInfo.chunkIndex(), "chunkInfo.token()"))
                .collect(Collectors.toSet());

        return ResponseEntity
                .ok(new GetTransferChannelTokensResponse(output.fileId(), output.expiresAt(), chunkTokens));

    }

}
