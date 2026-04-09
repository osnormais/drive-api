package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.isNull;
import static org.osnormais.drive.api.infrastructure.commons.RestLocationBuilder.buildLocation;

import java.util.List;
import java.util.UUID;

import org.osnormais.drive.api.application.usecase.file.create.CreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileInput;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.list.ListFileInput;
import org.osnormais.drive.api.application.usecase.file.retrieve.list.ListFileUseCase;
import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Filter.Operator;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.Pagination;
import org.osnormais.drive.api.domain.pagination.Pagination.Order.Direction;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.infrastructure.api.FileAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.osnormais.drive.api.infrastructure.file.data.rest.FileRequestMapper;
import org.osnormais.drive.api.infrastructure.file.data.rest.FileResponsePresenter;
import org.osnormais.drive.api.infrastructure.file.data.rest.GetFileResponse;
import org.osnormais.drive.api.infrastructure.file.data.rest.ListFileItemResponse;
import org.osnormais.drive.api.infrastructure.file.filter.FileField;
import org.osnormais.drive.api.infrastructure.filter.adapter.QueryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;
    private final GetFileUseCase getFileUseCase;
    private final ListFileUseCase listFilesUseCase;

    public FileController(
            final CreateFileUseCase createFileUseCase,
            final GetFileUseCase getFileUseCase,
            final ListFileUseCase listFilesUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.getFileUseCase = getFileUseCase;
        this.listFilesUseCase = listFilesUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateFileRequest request) {

        final var output = createFileUseCase
                .execute(FileRequestMapper.map(request, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity
                .created(buildLocation("/{id}", output.id()))
                .build();
    }

    @Override
    public ResponseEntity<GetFileResponse> getFile(final UUID id) {

        final var output = getFileUseCase.execute(new GetFileInput(id, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity
                .ok(FileResponsePresenter.map(output));
    }

    @Override
    public ResponseEntity<Page<ListFileItemResponse>> search(
            final int page,
            final int perPage,
            final FileField orderField,
            final Direction orderDirection,
            final Operator filterOperator,
            final List<String> filterGroups) {

        final List<Filter.Group> searchFilterGroups = isNull(filterGroups) ? List.of()
                : filterGroups
                        .stream()
                        .map(source -> QueryAdapter.of(
                                source,
                                List.of(FileField.values())))
                        .filter(group -> !group.elements().isEmpty())
                        .toList();

        final SearchQuery query = SearchQuery.of(
                Pagination.of(page, perPage, Pagination.Order.of(orderField, orderDirection)),
                filterOperator,
                searchFilterGroups);

        final var actorId = SecurityContext.getAuthenticatedUserId();

        return ResponseEntity
                .ok(listFilesUseCase.execute(new ListFileInput(query, actorId)).page().map(FileResponsePresenter::map));

    }

}
