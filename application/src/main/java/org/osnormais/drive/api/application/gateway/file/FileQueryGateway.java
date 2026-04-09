package org.osnormais.drive.api.application.gateway.file;

import java.util.Optional;
import java.util.Set;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.domain.user.UserId;

public interface FileQueryGateway {

    Optional<File> findById(FileId id);

    Optional<File> findVisibleById(FileId id, UserId userId);

    Size totalSizeByUserId(UserId userId);

    Boolean existsByFolderIdAndName(FolderId parentFolderId, FileName fileName);

    Set<File> findAllByFolder(FolderId id);

    Page<File> searchVisible(SearchQuery query, UserId userId);

}