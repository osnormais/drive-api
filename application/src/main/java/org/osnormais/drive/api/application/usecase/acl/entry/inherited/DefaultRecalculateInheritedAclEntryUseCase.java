package org.osnormais.drive.api.application.usecase.acl.entry.inherited;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.exception.DomainException;
import org.osnormais.drive.api.domain.exception.InconsistentStateException;
import org.osnormais.drive.api.domain.folder.FolderId;

public class DefaultRecalculateInheritedAclEntryUseCase extends RecalculateInheritedAclEntryUseCase {

    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final DomainEventDispatcher eventDispatcher;

    public DefaultRecalculateInheritedAclEntryUseCase(
            final AclQueryGateway aclQueryGateway,
            final AclCommandGateway aclCommandGateway,
            final FileQueryGateway fileQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.eventDispatcher = requireNonNull(eventDispatcher);
    }

    @Transactional
    @Override
    public void execute(final RecalculateInheritedAclEntryInput input) {

        final AclId aclId = new AclId(input.parentAclId());

        final Acl parentAcl = aclQueryGateway
                .findById(aclId)
                .orElseThrow(() -> InconsistentStateException.create(
                        Acl.class,
                        DomainException.Error.with("ACL [%s] not found".formatted(input.parentAclId()))));

        if (AclResourceType.FILE.equals(parentAcl.getResource().resourceType()))
            return;

        final FolderId parentFolderId = (FolderId) parentAcl.getResource().resourceId();

        fileQueryGateway
                .findAllByFolder(parentFolderId)
                .stream()
                .map(AclResource::of)
                .forEach(childResource -> recalculateInheritedAclEntries(parentAcl, childResource));

        folderQueryGateway
                .findAllByParent(parentFolderId)
                .stream()
                .map(AclResource::of)
                .forEach(childResource -> recalculateInheritedAclEntries(parentAcl, childResource));

    }

    private void recalculateInheritedAclEntries(final Acl parentAcl, final AclResource<?> childResource) {

        final Acl childAcl = aclQueryGateway
                .findByResource(childResource)
                .orElseThrow(() -> InconsistentStateException.create(
                        Acl.class,
                        DomainException.Error.with("ACL for resource [%s] not found".formatted(childResource))));

        eventDispatcher.dispatch(eventDispatcher.append(aclCommandGateway.update(childAcl.inheritFrom(parentAcl))));

    }

}
