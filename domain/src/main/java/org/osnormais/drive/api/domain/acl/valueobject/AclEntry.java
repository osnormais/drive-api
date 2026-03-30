package org.osnormais.drive.api.domain.acl.valueobject;

import static java.util.Objects.isNull;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record AclEntry(UserId user, Permission permission, Instant grantedAt) implements ValueObject {

    public static AclEntry create(final UserId user, final Permission permission) {
        return new AclEntry(user, permission, Instant.now());
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(user))
            handler.append(new ValidationError("'AclEntry.user' cannot be null."));
        else
            user.validate(handler);

        if (isNull(permission))
            handler.append(new ValidationError("'AclEntry.permission' cannot be null."));

        if (isNull(grantedAt))
            handler.append(new ValidationError("'AclEntry.grantedAt' cannot be null."));

    }

}
