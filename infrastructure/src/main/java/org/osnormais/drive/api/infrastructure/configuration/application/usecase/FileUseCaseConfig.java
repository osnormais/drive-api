package org.osnormais.drive.api.infrastructure.configuration.application.usecase;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.entitlement.grant.UserEntitlementGrantQueryGateway;
import org.osnormais.drive.api.application.gateway.entitlement.plan.PlanQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.application.usecase.file.create.CreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.create.DefaultCreateFileUseCase;
import org.osnormais.drive.api.application.usecase.file.publication.init.DefaultInitFilePublicationUseCase;
import org.osnormais.drive.api.application.usecase.file.publication.init.InitFilePublicationUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.DefaultGetFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.get.GetFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.list.DefaultListFileUseCase;
import org.osnormais.drive.api.application.usecase.file.retrieve.list.ListFileUseCase;
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
    private final PlanQueryGateway planQueryGateway;
    private final UserEntitlementGrantQueryGateway userEntitlementGrantQueryGateway;
    private final DomainEventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            UserQueryGateway userQueryGateway,
            FolderQueryGateway folderQueryGateway,
            FileQueryGateway fileQueryGateway,
            FileCommandGateway fileCommandGateway,
            AclQueryGateway aclQueryGateway,
            AclCommandGateway aclCommandGateway,
            PlanQueryGateway planQueryGateway,
            UserEntitlementGrantQueryGateway userEntitlementGrantQueryGateway,
            DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.planQueryGateway = requireNonNull(planQueryGateway);
        this.userEntitlementGrantQueryGateway = requireNonNull(userEntitlementGrantQueryGateway);
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
                planQueryGateway,
                userEntitlementGrantQueryGateway,
                eventDispatcher);
    }

    @Bean
    InitFilePublicationUseCase initFilePublicationUseCase() {
        return new DefaultInitFilePublicationUseCase(
                fileQueryGateway,
                fileCommandGateway,
                eventDispatcher);
    }

    @Bean
    GetFileUseCase getFileUseCase() {
        return new DefaultGetFileUseCase(
                fileQueryGateway,
                aclQueryGateway);
    }

    @Bean
    ListFileUseCase listFileUseCase() {
        return new DefaultListFileUseCase(fileQueryGateway);
    }

}
