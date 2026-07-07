package org.osnormais.drive.api.domain.exception;

import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.user.UserId;

public class AccessDeniedException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "User [%s] does not have required permission [%s] for resource [%s]";

    private AccessDeniedException(final UserId user, final Permission permission, final AclResource<?> resource) {
        super(formatMessage(user, permission, resource));
    }

    public static AccessDeniedException with(
            final UserId user,
            final Permission permission,
            final AclResource<?> resource) {
        return new AccessDeniedException(user, permission, resource);
    }

    private static String formatMessage(final UserId user, final Permission permission, final AclResource<?> resource) {
        return MESSAGE_TEMPLATE
                .formatted(
                        user.getStringValue(),
                        permission,
                        resource.resourceType() + ":" + resource.resourceId().getStringValue());
    }

}
