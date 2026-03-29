package org.osnormais.drive.api.application.usecase.file.create;

import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final FileCommandGateway fileCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public DefaultCreateFileUseCase(
            final FileCommandGateway fileCommandGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.fileCommandGateway = fileCommandGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final FileName fileName = FileName.of(input.name());
        final Content content = Content.of(input.contentType());
        final Size size = Size.of(input.sizeInBytes());
        final Checksum checksum = Checksum.of(input.checksumAlgorithm(), input.checksumValue());

        final ValidationHandler handler = Notification.create();

        fileName.validate(handler);
        size.validate(handler);
        checksum.validate(handler);
        content.validate(handler);

        if (handler.hasErrors())
            throw ValidationException.with("Invalid input values", handler);

        final File file = File.create(fileName, checksum, size, content);

        eventDispatcher.notify(fileCommandGateway.create(file));

        return CreateFileOutput.of(file);

    }

}
