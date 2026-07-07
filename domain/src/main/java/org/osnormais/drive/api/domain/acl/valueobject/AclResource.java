package org.osnormais.drive.api.domain.acl.valueobject;

import static java.util.Objects.isNull;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;
import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record AclResource<I extends Identifier<UUID>>(
        I resourceId,
        AclResourceType resourceType,
        UserId owner) implements ValueObject {

    public static AclResource<FileId> of(final File file) {
        return new AclResource<>(file.getId(), AclResourceType.FILE, file.getOwner());
    }

    public static AclResource<FolderId> of(final Folder folder) {
        return new AclResource<>(folder.getId(), AclResourceType.FOLDER, folder.getOwner());
    }

    public static AclResource<FileId> create(final FileId fileId, UserId owner) {
        return new AclResource<>(fileId, AclResourceType.FILE, owner);
    }

    public static AclResource<FolderId> create(final FolderId folderId, UserId owner) {
        return new AclResource<>(folderId, AclResourceType.FOLDER, owner);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (isNull(resourceId))
            handler.append(new ValidationError("'AclResource.resourceId' cannot be null."));
        else
            resourceId.validate(handler);

        if (isNull(resourceType))
            handler.append(new ValidationError("'AclResource.resourceType' cannot be null."));
        else if (!resourceType.isValidResourceId(resourceId))
            handler.append(new ValidationError(
                    "'AclResource.resourceId' is not valid for the specified 'AclResource.resourceType'."));

        if (isNull(owner))
            handler.append(new ValidationError("'AclResource.owner' cannot be null."));
        else
            owner.validate(handler);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
        result = prime * result + ((resourceType == null) ? 0 : resourceType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        AclResource other = (AclResource) obj;
        if (resourceId == null) {
            if (other.resourceId != null)
                return false;
        } else if (!resourceId.equals(other.resourceId))
            return false;
        if (resourceType != other.resourceType)
            return false;
        return true;
    }

}
