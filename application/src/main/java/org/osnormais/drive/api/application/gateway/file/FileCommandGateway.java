package org.osnormais.drive.api.application.gateway.file;

import org.osnormais.drive.api.domain.file.File;

public interface FileCommandGateway {

    File create(File file);

    File update(File file);

}
