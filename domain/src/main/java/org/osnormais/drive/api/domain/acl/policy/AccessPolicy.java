package org.osnormais.drive.api.domain.acl.policy;

import java.security.Permission;

import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.user.UserId;

@FunctionalInterface
public interface AccessPolicy {

    void requirePermission(Acl acl, UserId user, Permission permission);

}
