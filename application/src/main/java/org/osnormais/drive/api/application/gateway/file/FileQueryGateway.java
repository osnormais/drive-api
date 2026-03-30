package org.osnormais.drive.api.application.gateway.file;

import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

public interface FileQueryGateway {

    Size totalSizeByUserId(UserId userId);

    Boolean existsByFolderIdAndName(FolderId parentFolderId, FileName fileName);

}