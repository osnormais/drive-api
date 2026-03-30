package org.osnormais.drive.api.infrastructure.api;

import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.springframework.http.ResponseEntity;
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

}
