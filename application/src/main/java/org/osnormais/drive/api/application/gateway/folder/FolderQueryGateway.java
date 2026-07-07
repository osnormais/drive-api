package org.osnormais.drive.api.application.gateway.folder;

import java.util.Optional;
import java.util.Set;

import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.domain.user.UserId;

public interface FolderQueryGateway {

    Optional<Folder> findByOwnerAndType(UserId ownerId, FolderType type);

    Optional<Folder> findVisibleById(FolderId id, UserId userId);

    Boolean existsByParentIdAndName(FolderId parentFolderId, FolderName name);

    Set<Folder> findAllByParent(FolderId id);

    Page<Folder> searchVisible(SearchQuery query, UserId userId);

}