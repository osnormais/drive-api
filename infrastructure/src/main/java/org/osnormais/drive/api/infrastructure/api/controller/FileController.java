package org.osnormais.drive.api.infrastructure.api.controller;

import static org.osnormais.drive.api.infrastructure.commons.RestLocationBuilder.buildLocation;

import org.osnormais.drive.api.application.usecase.file.create.CreateFileUseCase;
import org.osnormais.drive.api.infrastructure.api.FileAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.osnormais.drive.api.infrastructure.file.data.rest.FileRequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;

    public FileController(final CreateFileUseCase createFileUseCase) {
        this.createFileUseCase = createFileUseCase;
    }

    @Override
    public ResponseEntity<Void> create(final CreateFileRequest request) {

        final var output = createFileUseCase
                .execute(FileRequestMapper.map(request, SecurityContext.getAuthenticatedUserId()));

        return ResponseEntity
                .created(buildLocation("/{id}", output.id()))
                .build();
    }

}
