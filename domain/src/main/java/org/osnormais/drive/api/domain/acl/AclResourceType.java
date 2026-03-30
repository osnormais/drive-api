package org.osnormais.drive.api.domain.acl;

import org.osnormais.drive.api.domain.Entity;
import org.osnormais.drive.api.domain.Identifier;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;

public enum AclResourceType {

    FILE(File.class, FileId.class),
    FOLDER(Folder.class, FolderId.class);

    private final Class<? extends Identifier<?>> identifierClass;
    private final Class<? extends Entity<?>> entityClass;

    AclResourceType(
            Class<? extends Entity<?>> entityClass,
            Class<? extends Identifier<?>> identifierClass) {
        this.entityClass = entityClass;
        this.identifierClass = identifierClass;
    }

    public Class<? extends Identifier<?>> getIdentifierClass() {
        return identifierClass;
    }

    public Class<? extends Entity<?>> getEntityClass() {
        return entityClass;
    }

    public Boolean isValidResourceId(final Identifier<?> resourceId) {
        return identifierClass.isInstance(resourceId);
    }

}