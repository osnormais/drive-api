package org.osnormais.drive.api.infrastructure.api;

import java.util.UUID;

import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.osnormais.drive.api.infrastructure.file.data.rest.GetFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Files")
@RequestMapping("files")
public interface FileAPI {

    @Operation(summary = "Create file", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateFileRequest request);

    @Operation(summary = "Get file", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}")
    ResponseEntity<GetFileResponse> getFile(@PathVariable("id") UUID id);

}
