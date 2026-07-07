package org.osnormais.drive.api.infrastructure.api;

import java.util.List;
import java.util.UUID;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.Pagination;
import org.osnormais.drive.api.infrastructure.folder.data.rest.CreateFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.data.rest.GetFolderResponse;
import org.osnormais.drive.api.infrastructure.folder.data.rest.ListFolderItemResponse;
import org.osnormais.drive.api.infrastructure.folder.data.rest.ShareFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.filter.FolderField;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Folders")
@RequestMapping("folders")
public interface FolderAPI {

    @Operation(summary = "Get root folder", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("root")
    ResponseEntity<GetFolderResponse> getRootFolder();

    @Operation(summary = "Get inbox folder", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("inbox")
    ResponseEntity<GetFolderResponse> getInboxFolder();

    @Operation(summary = "Get folder", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}")
    ResponseEntity<GetFolderResponse> getFolder(@PathVariable("id") UUID id);

    @Operation(summary = "Search folders", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    ResponseEntity<Page<ListFolderItemResponse>> search(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "CREATED_AT") FolderField orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") Pagination.Order.Direction orderDirection,
            @RequestParam(name = "filterOperator", required = false, defaultValue = "AND") Filter.Operator filterOperator,
            @RequestParam(name = "filterGroups", required = false) List<String> filterGroups);

    @Operation(summary = "Create folder", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateFolderRequest request);

    @Operation(summary = "Share folder", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("{id}/sharings")
    ResponseEntity<Void> share(@PathVariable("id") UUID id, @RequestBody ShareFolderRequest request);

}
