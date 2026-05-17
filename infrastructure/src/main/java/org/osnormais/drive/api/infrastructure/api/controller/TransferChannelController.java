package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelInput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelInput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelUseCase;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.infrastructure.api.TransferChannelAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferChannelController implements TransferChannelAPI {

    private final CreateTransferChannelUseCase createTransferChannelUseCase;
    private final GetTransferChannelUseCase getTransferChannelUseCase;

    public TransferChannelController(
            final CreateTransferChannelUseCase createTransferChannelUseCase,
            final GetTransferChannelUseCase getTransferChannelUseCase) {
        this.createTransferChannelUseCase = requireNonNull(createTransferChannelUseCase);
        this.getTransferChannelUseCase = requireNonNull(getTransferChannelUseCase);
    }

    @Override
    public ResponseEntity<GetTransferChannelTokenResponse> createTrasnferChannel(
            final UUID fileId,
            final TransferChannelType type) {

        final CreateTransferChannelOutput output = createTransferChannelUseCase.execute(
                new CreateTransferChannelInput(SecurityContext.getAuthenticatedUserId(), fileId, type));

        return ResponseEntity.ok(new GetTransferChannelTokenResponse(output.id(), output.expiresAt(), "token"));

    }

    @Override
    public ResponseEntity<GetTransferChannelTokenResponse> getTransferChannelToken(final UUID id) {

        final GetTransferChannelOutput output = getTransferChannelUseCase
                .execute(new GetTransferChannelInput(SecurityContext.getAuthenticatedUserId(), id));

        return ResponseEntity.ok(new GetTransferChannelTokenResponse(output.id(), output.expiresAt(), "token"));

    }

}
