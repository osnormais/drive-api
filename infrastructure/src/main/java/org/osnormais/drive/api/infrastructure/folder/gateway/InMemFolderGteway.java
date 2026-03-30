package org.osnormais.drive.api.infrastructure.folder.gateway;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
// @ConditionalOnProperty(name = "application.vendor.database", havingValue = "inmemory")
public class InMemFolderGteway implements FolderCommandGateway, FolderQueryGateway {

    private static final ConcurrentHashMap<FolderId, Folder> datasource = new ConcurrentHashMap<>();

    @Override
    public Optional<Folder> findById(FolderId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Optional<Folder> findVisibleById(FolderId id, UserId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findVisibleById'");
    }

    @Override
    public Folder create(Folder folder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Folder update(Folder folder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
