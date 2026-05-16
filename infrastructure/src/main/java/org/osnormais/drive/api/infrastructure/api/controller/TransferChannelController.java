package org.osnormais.drive.api.infrastructure.api.controller;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelInput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelOutput;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelUseCase;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.infrastructure.api.TransferChannelAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.CreateTransferChannelResponse;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransferChannelController implements TransferChannelAPI {

    private final CreateTransferChannelUseCase createTransferChannelUseCase;

    public TransferChannelController(final CreateTransferChannelUseCase createTransferChannelUseCase) {
        this.createTransferChannelUseCase = createTransferChannelUseCase;
    }

    @Override
    public ResponseEntity<CreateTransferChannelResponse> createTrasnferChannel(
            final UUID idempotencyKey,
            final UUID fileId,
            final TransferChannelType type) {

        final CreateTransferChannelOutput output = createTransferChannelUseCase.execute(
                new CreateTransferChannelInput(SecurityContext.getAuthenticatedUserId(), fileId, type));

        return ResponseEntity.ok(new CreateTransferChannelResponse(output.id()));

    }

    @Override
    public ResponseEntity<GetTransferChannelTokenResponse> getTransferChannelToken(final UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransferChannelToken'");
    }

}
