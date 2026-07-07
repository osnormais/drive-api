package org.osnormais.drive.api.application.usecase.file.create;

import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Checksum;

public record CreateFileInput(
        UUID creatorId,
        UUID parentFolderId,
        String name,
        String contentType,
        Long sizeInBytes,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm) {

}
