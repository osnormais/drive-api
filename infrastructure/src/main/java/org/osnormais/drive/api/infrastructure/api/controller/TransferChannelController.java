package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelUseCase;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.infrastructure.api.TransferChannelAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.token.TokenGenerator;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.ChunkToken;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelResponse;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelTokensResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferChannelController implements TransferChannelAPI {

    private final TokenGenerator tokenGenerator;

    private final CreateTransferChannelUseCase createTransferChannelUseCase;
    private final GetTransferChannelUseCase getTransferChannelUseCase;
    private final GetTransferChannelPermissionUseCase getTransferChannelPermissionUseCase;

    public TransferChannelController(
            final TokenGenerator tokenGenerator,
            final CreateTransferChannelUseCase createTransferChannelUseCase,
            final GetTransferChannelUseCase getTransferChannelUseCase,
            final GetTransferChannelPermissionUseCase getTransferChannelPermissionUseCase) {
        this.tokenGenerator = requireNonNull(tokenGenerator);
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
                .filter(range -> range.length == 2)
                .map(range -> new Range(Long.parseLong(range[0]), Long.parseLong(range[1])))
                .collect(Collectors.toSet());

        final UUID authenticatedUserId = SecurityContext.getAuthenticatedUserId();

        final GetTransferChannelPermissionOutput output = getTransferChannelPermissionUseCase
                .execute(new GetTransferChannelPermissionInput(authenticatedUserId, id, rangeSet));

        final Set<ChunkToken> chunkTokens = output
                .chunks()
                .stream()
                .map(chunkInfo -> new ChunkToken(
                        chunkInfo.chunkIndex(),
                        generateToken(
                                output.actorId(),
                                output.expiresAt(),
                                output.fileId(),
                                output.type(),
                                chunkInfo.chunkIndex(),
                                chunkInfo.chunkOffset(),
                                chunkInfo.chunkSize())))
                .collect(Collectors.toSet());

        return ResponseEntity
                .ok(new GetTransferChannelTokensResponse(output.fileId(), output.expiresAt(), chunkTokens));

    }

    private String generateToken(
            UUID actor,
            Instant expiresAt,
            UUID fileId,
            String type,
            Long chunkIndex,
            Long chunkOffset,
            Long chunkSize) {

        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("actor", actor.toString());
                put("file", fileId.toString());
                put("type", type);
                put("chunkIndex", chunkIndex);
                put("chunkOffset", chunkOffset);
                put("chunkSize", chunkSize);
            }
        };

        return tokenGenerator.generate(map, expiresAt);

    }

}
