package org.osnormais.drive.api.infrastructure.file.gateway;

import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpa;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileJpaGateway implements FileCommandGateway, FileQueryGateway {

    private final FileJpaRepository fileRepository;

    public FileJpaGateway(final FileJpaRepository fileJpaRepository) {
        this.fileRepository = fileJpaRepository;
    }

    @Override
    public Size totalSizeByUserId(final UserId userId) {
        return Size.of(fileRepository.sumSizeInBytesByOwnerId(userId.getValue()));
    }

    @Override
    public Boolean existsByFolderIdAndName(final FolderId parentFolderId, final FileName fileName) {
        return fileRepository.existsByNameAndFolderId(fileName.value(), parentFolderId.getValue());
    }

    @Transactional
    @Override
    public File create(final File file) {

        if (fileRepository.existsById(file.getId().getValue()))
            throw new IllegalStateException("File with id %s already exists".formatted(file.getId().getStringValue()));

        save(file);

        return file;
    }

    @Transactional
    @Override
    public File update(final File file) {
        if (!fileRepository.existsById(file.getId().getValue()))
            throw new IllegalStateException("File with id %s does not exist".formatted(file.getId().getStringValue()));

        save(file);

        return file;
    }

    private void save(final File file) {
        fileRepository.save(FileJpa.fromDomain(file));
    }

}
