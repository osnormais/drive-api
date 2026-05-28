package org.osnormais.drive.api.infrastructure.api;

import java.util.UUID;

import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.CreateTransferChannelResponse;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelResponse;
import org.osnormais.drive.api.infrastructure.transferchannel.data.rest.GetTransferChannelTokensResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transfer Channels")
@RequestMapping("transfer-channels")
public interface TransferChannelAPI {

    @Operation(summary = "Create transfer channel", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<CreateTransferChannelResponse> createTrasnferChannel(
            @RequestHeader("X-File-Id") UUID fileId,
            @RequestHeader("X-Type") TransferChannelType type);

    @Operation(summary = "Retrieve transfer channel", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}")
    ResponseEntity<GetTransferChannelResponse> getTransferChannel(@PathVariable("id") UUID id);

    @Operation(summary = "Retrieve transfer channel token", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}/chunks/permissions/tokens")
    ResponseEntity<GetTransferChannelTokensResponse> getTransferChannelChunksPermissionTokens(
            @PathVariable("id") UUID id,
            @RequestHeader(value = "X-Ranges", required = true) String ranges);

}
