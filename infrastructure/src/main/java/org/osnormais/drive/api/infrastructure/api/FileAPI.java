package org.osnormais.drive.api.infrastructure.api;

import java.util.List;
import java.util.UUID;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.Pagination;
import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.osnormais.drive.api.infrastructure.file.data.rest.GetFileResponse;
import org.osnormais.drive.api.infrastructure.file.data.rest.ListFileItemResponse;
import org.osnormais.drive.api.infrastructure.file.filter.FileField;
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

@Tag(name = "Files")
@RequestMapping("files")
public interface FileAPI {

    @Operation(summary = "Create file", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateFileRequest request);

    @Operation(summary = "Get file", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("{id}")
    ResponseEntity<GetFileResponse> getFile(@PathVariable("id") UUID id);

    @Operation(summary = "Search files", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    ResponseEntity<Page<ListFileItemResponse>> search(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "CREATED_AT") FileField orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") Pagination.Order.Direction orderDirection,
            @RequestParam(name = "filterOperator", required = false, defaultValue = "AND") Filter.Operator filterOperator,
            @RequestParam(name = "filterGroups", required = false) List<String> filterGroups);

}
