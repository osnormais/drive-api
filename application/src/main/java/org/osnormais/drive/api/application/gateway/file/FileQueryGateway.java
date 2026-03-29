package org.osnormais.drive.api.application.gateway.file;

import java.util.Optional;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;

public interface FileQueryGateway {

    Optional<File> findById(FileId id);

}