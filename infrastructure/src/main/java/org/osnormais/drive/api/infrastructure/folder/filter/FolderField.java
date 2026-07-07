package org.osnormais.drive.api.infrastructure.folder.filter;

import java.util.Set;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Filter.Field;

public enum FolderField implements Field {
    ID("id", Set.of(Filter.Type.EQUALS)),
    CREATOR_ID("creatorId", Set.of(Filter.Type.EQUALS)),
    OWNER_ID("ownerId", Set.of(Filter.Type.EQUALS)),
    PARENT_FOLDER_ID("parentFolderId", Set.of(Filter.Type.EQUALS)),
    NAME("name", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    CREATED_AT("createdAt", Set.of(Filter.Type.BETWEEN)),
    UPDATED_AT("updatedAt", Set.of(Filter.Type.BETWEEN));

    private final String fieldName;
    private final Set<Filter.Type> supportedTypes;

    FolderField(final String fieldName, final Set<Filter.Type> supportedTypes) {
        this.fieldName = fieldName;
        this.supportedTypes = supportedTypes;
    }

    public String value() {
        return fieldName;
    }

    public Set<Filter.Type> supportedFilterTypes() {
        return supportedTypes;
    }

}
