package org.osnormais.drive.api.infrastructure.api;

import org.osnormais.drive.api.infrastructure.folder.data.rest.CreateFolderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Folders")
@RequestMapping("folders")
public interface FolderAPI {

    @Operation(summary = "Create folder", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateFolderRequest request);

}
