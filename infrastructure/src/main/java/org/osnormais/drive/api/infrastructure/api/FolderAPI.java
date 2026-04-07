package org.osnormais.drive.api.infrastructure.api;

import java.util.UUID;

import org.osnormais.drive.api.infrastructure.folder.data.rest.CreateFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.data.rest.GetFolderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Folders")
@RequestMapping("folders")
public interface FolderAPI {

    @Operation(summary = "Get root folder", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("root")
    ResponseEntity<GetFolderResponse> getRootFolder();

    @Operation(summary = "Get folder", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}")
    ResponseEntity<GetFolderResponse> getFolder(@PathVariable("id") UUID id);

    @Operation(summary = "Create folder", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateFolderRequest request);

}
