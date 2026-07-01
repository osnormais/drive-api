package org.osnormais.drive.api.infrastructure.file.data.message.command;

import java.io.Serializable;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Publication;

public record FinalizeFilePublicationCommand(
        UUID fileId,
        Publication.Status status,
        String errorMessage) implements Serializable {

}
