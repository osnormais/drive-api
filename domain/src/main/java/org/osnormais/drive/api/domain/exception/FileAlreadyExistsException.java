package org.osnormais.drive.api.domain.exception;

import java.util.List;

import org.osnormais.drive.api.domain.file.valueobject.FileName;

public class FileAlreadyExistsException extends SilentDomainException {

    private FileAlreadyExistsException(final FileName fileName) {
        super(
                "File name [" + fileName.value() + "] already exists.",
                List.of(DomainException.Error.with(
                        "A file with the same name already exists in the target folder. Please choose a different name or remove the existing file.")));
    }

    public static FileAlreadyExistsException with(final FileName fileName) {
        return new FileAlreadyExistsException(fileName);
    }
}
