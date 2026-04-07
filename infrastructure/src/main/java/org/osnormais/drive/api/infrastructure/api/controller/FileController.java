package org.osnormais.drive.api.infrastructure.api.controller;

import static org.osnormais.drive.api.infrastructure.commons.RestLocationBuilder.buildLocation;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.file.create.CreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileInput;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileUseCase;
import org.osnormais.drive.api.infrastructure.api.FileAPI;
import org.osnormais.drive.api.infrastructure.commons.SecurityContext;
import org.osnormais.drive.api.infrastructure.file.data.rest.CreateFileRequest;
import org.osnormais.drive.api.infrastructure.file.data.rest.FileRequestMapper;
import org.osnormais.drive.api.infrastructure.file.data.rest.FileResponsePresenter;
import org.osnormais.drive.api.infrastructure.file.data.rest.GetFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController implements FileAPI {

    private final CreateFileUseCase createFileUseCase;
    private final GetFileUseCase getFileUseCase;

    public FileController(
            final CreateFileUseCase createFileUseCase,
            final GetFileUseCase getFileUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.getFileUseCase = getFileUseCase;
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

}
