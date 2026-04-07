package org.osnormais.drive.api.infrastructure.configuration.application.usecase;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.application.usecase.file.create.CreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.create.DefaultCreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.DefaultGetFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileUseCase;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUseCaseConfig {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;
    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            UserQueryGateway userQueryGateway,
            FolderQueryGateway folderQueryGateway,
            FileQueryGateway fileQueryGateway,
            FileCommandGateway fileCommandGateway,
            AclQueryGateway aclQueryGateway,
            AclCommandGateway aclCommandGateway,
            DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.eventDispatcher = requireNonNull(eventDispatcher);
    }

    @Bean
    CreateFileUseCase createFileUseCase() {
        return new DefaultCreateFileUseCase(
                userQueryGateway,
                folderQueryGateway,
                fileQueryGateway,
                fileCommandGateway,
                aclQueryGateway,
                aclCommandGateway,
                eventDispatcher);
    }

    @Bean
    GetFileUseCase getFileUseCase() {
        return new DefaultGetFileUseCase(
                fileQueryGateway,
                aclQueryGateway);
    }

}
