package org.osnormais.drive.api.application.usecase.file.retrieve.get;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.exception.DomainException;
import org.osnormais.drive.api.domain.exception.InconsistentStateException;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetFileUseCase extends GetFileUseCase {

    private final FileQueryGateway fileQueryGateway;
    private final AclQueryGateway aclQueryGateway;

    public DefaultGetFileUseCase(
            final FileQueryGateway fileQueryGateway,
            final AclQueryGateway aclQueryGateway) {
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
    }

    @Transactional
    @Override
    public GetFileOutput execute(final GetFileInput input) {

        final FileId fileId = FileId.of(input.fileId());
        final UserId actorId = UserId.of(input.actorId());

        final File file = fileQueryGateway
                .findVisibleById(fileId, actorId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final Acl acl = aclQueryGateway
                .findByResource(AclResource.of(file))
                .orElseThrow(() -> InconsistentStateException.create(
                        Acl.class,
                        List.of(DomainException.Error.with("ACL not found for file [%s]".formatted(fileId)))));

        acl.requiredPermission(actorId, Permission.READ);

        return GetFileOutput.from(file, actorId);
    }

}
