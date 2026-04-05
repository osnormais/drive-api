package org.osnormais.drive.api.infrastructure.configuration.application.usecase;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderUseCase;
import org.osnormais.drive.api.application.usecase.folder.create.DefaultCreateFolderUseCase;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FolderUseCaseConfig {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public FolderUseCaseConfig(
            final UserQueryGateway userQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final FolderCommandGateway folderCommandGateway,
            final AclQueryGateway aclQueryGateway,
            final AclCommandGateway aclCommandGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.folderCommandGateway = requireNonNull(folderCommandGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.eventDispatcher = requireNonNull(eventDispatcher);
    }

    @Bean
    CreateFolderUseCase createFolderUseCase() {
        return new DefaultCreateFolderUseCase(
                userQueryGateway,
                folderQueryGateway,
                folderCommandGateway,
                aclQueryGateway,
                aclCommandGateway,
                eventDispatcher);
    }

}
