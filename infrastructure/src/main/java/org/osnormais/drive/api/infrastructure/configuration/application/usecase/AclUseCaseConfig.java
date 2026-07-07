package org.osnormais.drive.api.infrastructure.configuration.application.usecase;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.usecase.acl.entry.inherited.DefaultRecalculateInheritedAclEntryUseCase;
import org.osnormais.drive.api.application.usecase.acl.entry.inherited.RecalculateInheritedAclEntryUseCase;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AclUseCaseConfig {

    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final DomainEventDispatcher eventDispatcher;

    public AclUseCaseConfig(
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

    @Bean
    RecalculateInheritedAclEntryUseCase recalculateInheritedAclEntryUseCase() {
        return new DefaultRecalculateInheritedAclEntryUseCase(
                aclQueryGateway,
                aclCommandGateway,
                fileQueryGateway,
                folderQueryGateway,
                eventDispatcher);
    }

}
