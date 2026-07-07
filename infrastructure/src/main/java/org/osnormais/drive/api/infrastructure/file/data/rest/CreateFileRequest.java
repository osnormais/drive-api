package org.osnormais.drive.api.infrastructure.file.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Checksum;

public record CreateFileRequest(
        UUID parentFolderId,
        String name,
        String contentType,
        Long sizeInBytes,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm) {

}
