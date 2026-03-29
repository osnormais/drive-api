package org.osnormais.drive.api.application.usecase.file.create;

import org.osnormais.drive.api.domain.file.valueobject.Checksum;

public record CreateFileInput(
        String name,
        String contentType,
        Long sizeInBytes,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm) {

}
