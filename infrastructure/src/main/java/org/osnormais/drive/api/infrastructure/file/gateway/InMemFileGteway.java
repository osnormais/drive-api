package org.osnormais.drive.api.infrastructure.file.gateway;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "application.vendor.database", havingValue = "inmemory")
public class InMemFileGteway implements FileCommandGateway, FileQueryGateway {

    private static final ConcurrentHashMap<FileId, File> datasource = new ConcurrentHashMap<>();

    @Override
    public Optional<File> findById(final FileId id) {
        return Optional.ofNullable(datasource.get(id));
    }

    @Override
    public File create(final File file) {

        if (datasource.containsKey(file.getId()))
            throw new IllegalStateException("File with id %s already exists".formatted(file.getId().getStringValue()));

        datasource.put(file.getId(), file);

        return file;
    }

    @Override
    public File update(final File file) {

        if (!datasource.containsKey(file.getId()))
            throw new IllegalStateException("File with id %s does not exist".formatted(file.getId().getStringValue()));

        datasource.put(file.getId(), file);

        return file;
    }

    @Override
    public Size totalSizeByUserId(UserId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'totalSizeByUserId'");
    }

    @Override
    public Boolean existsByFolderIdAndName(FolderId parentFolderId, FileName fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByFolderIdAndName'");
    }

}
