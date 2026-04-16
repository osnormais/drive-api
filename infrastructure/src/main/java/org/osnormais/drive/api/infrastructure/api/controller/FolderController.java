package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.osnormais.drive.api.infrastructure.commons.RestLocationBuilder.buildLocation;

import java.util.List;
import java.util.UUID;

import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.GetFolderInput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.GetFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.inbox.GetInboxFolderInput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.inbox.GetInboxFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.root.GetRootFolderInput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.root.GetRootFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.retrieve.list.ListFolderInput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.list.ListFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.sharings.create.ShareFolderUseCase;
import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Filter.Operator;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.Pagination;
import org.osnormais.drive.api.domain.pagination.Pagination.Order.Direction;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.infrastructure.api.FolderAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.file.filter.FileField;
import org.osnormais.drive.api.infrastructure.filter.adapter.QueryAdapter;
import org.osnormais.drive.api.infrastructure.folder.data.rest.CreateFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.data.rest.FolderRequestMapper;
import org.osnormais.drive.api.infrastructure.folder.data.rest.FolderResponsePresenter;
import org.osnormais.drive.api.infrastructure.folder.data.rest.GetFolderResponse;
import org.osnormais.drive.api.infrastructure.folder.data.rest.ListFolderItemResponse;
import org.osnormais.drive.api.infrastructure.folder.data.rest.ShareFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.filter.FolderField;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FolderController implements FolderAPI {

    private final GetRootFolderUseCase getRootFolderUseCase;
    private final GetInboxFolderUseCase getInboxFolderUseCase;
    private final GetFolderUseCase getFolderUseCase;
    private final ListFolderUseCase listFolderUseCase;
    private final CreateFolderUseCase createFolderUseCase;
    private final ShareFolderUseCase shareFolderUseCase;

    public FolderController(
            final GetRootFolderUseCase getRootFolderUseCase,
            final GetInboxFolderUseCase getInboxFolderUseCase,
            final GetFolderUseCase getFolderUseCase,
            final ListFolderUseCase listFolderUseCase,
            final CreateFolderUseCase createFolderUseCase,
            final ShareFolderUseCase shareFolderUseCase) {
        this.getRootFolderUseCase = requireNonNull(getRootFolderUseCase);
        this.getInboxFolderUseCase = requireNonNull(getInboxFolderUseCase);
        this.getFolderUseCase = requireNonNull(getFolderUseCase);
        this.listFolderUseCase = requireNonNull(listFolderUseCase);
        this.createFolderUseCase = requireNonNull(createFolderUseCase);
        this.shareFolderUseCase = requireNonNull(shareFolderUseCase);
    }

    @Override
    public ResponseEntity<GetFolderResponse> getRootFolder() {

        final var output = getRootFolderUseCase
                .execute(new GetRootFolderInput(SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity.ok(FolderResponsePresenter.map(output));
    }

    @Override
    public ResponseEntity<GetFolderResponse> getInboxFolder() {

        final var output = getInboxFolderUseCase
                .execute(new GetInboxFolderInput(SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity.ok(FolderResponsePresenter.map(output));
    }

    @Override
    public ResponseEntity<GetFolderResponse> getFolder(final UUID id) {

        final var output = getFolderUseCase
                .execute(new GetFolderInput(id, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity.ok(FolderResponsePresenter.map(output));
    }

    @Override
    public ResponseEntity<Page<ListFolderItemResponse>> search(
            int page,
            int perPage,
            FolderField orderField,
            Direction orderDirection,
            Operator filterOperator,
            List<String> filterGroups) {

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
                .ok(listFolderUseCase
                        .execute(new ListFolderInput(query, actorId))
                        .page()
                        .map(FolderResponsePresenter::map));
    }

    @Override
    public ResponseEntity<Void> create(final CreateFolderRequest request) {

        final var output = createFolderUseCase
                .execute(FolderRequestMapper.map(request, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity
                .created(buildLocation("/{id}", output.id()))
                .build();
    }

    @Override
    public ResponseEntity<Void> share(final UUID id, final ShareFolderRequest request) {

        shareFolderUseCase.execute(FolderRequestMapper.map(request, SecurityContext.getAuthenticatedUserId(), id));

        return ResponseEntity
                .noContent()
                .build();
    }

}
