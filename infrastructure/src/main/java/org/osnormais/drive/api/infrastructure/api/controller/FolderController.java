package org.osnormais.drive.api.infrastructure.api.controller;

import static java.util.Objects.requireNonNull;
import static org.osnormais.drive.api.infrastructure.commons.RestLocationBuilder.buildLocation;

import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderUseCase;
import org.osnormais.drive.api.infrastructure.api.FolderAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.folder.data.rest.CreateFolderRequest;
import org.osnormais.drive.api.infrastructure.folder.data.rest.FolderRequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FolderController implements FolderAPI {

    private final CreateFolderUseCase createFolderUseCase;

    public FolderController(final CreateFolderUseCase createFolderUseCase) {
        this.createFolderUseCase = requireNonNull(createFolderUseCase);
    }

    @Override
    public ResponseEntity<Void> create(final CreateFolderRequest request) {

        final var output = createFolderUseCase
                .execute(FolderRequestMapper.map(request, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity
                .created(buildLocation("/{id}", output.id()))
                .build();
    }

}
