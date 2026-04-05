package org.osnormais.drive.api.application.usecase.folder.create;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEventContext;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.user.valueobject.Quota;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFolderUseCaseTest {

    @InjectMocks
    DefaultCreateFolderUseCase useCase;

    @Mock
    UserQueryGateway userQueryGateway;

    @Mock
    FolderQueryGateway folderQueryGateway;

    @Mock
    FolderCommandGateway folderCommandGateway;

    @Mock
    AclQueryGateway aclQueryGateway;

    @Mock
    AclCommandGateway aclCommandGateway;

    @Mock
    DomainEventDispatcher eventDispatcher;

    @Test
    void givenAnValidInput_whenCallsExecute_thenShouldCreateFolder() {

        final var now = Instant.now();

        final var expectedCreatorIdValue = UUID.randomUUID();
        final var expectedParentIdValue = UUID.randomUUID();
        final var expectedNameValue = "folder name";

        final var expectedCreatorId = UserId.of(expectedCreatorIdValue);
        final var expectedCreator = User.with(
                expectedCreatorId,
                Quota.of(1024L),
                null,
                null);

        final var expectedParentFolderId = FolderId.of(expectedParentIdValue);
        final var expectedParentFolder = Folder.with(
                expectedParentFolderId,
                expectedCreatorId,
                expectedCreatorId,
                expectedParentFolderId,
                FolderName.of("parent folder name"),
                now.minus(1L, ChronoUnit.HOURS),
                now.minus(5L, ChronoUnit.MINUTES),
                null,
                null,
                null);

        final var expectedAclIdValue = UUID.randomUUID();
        final var expectedAclId = AclId.of(expectedAclIdValue);
        final var expectedAcl = Acl.with(
                expectedAclId,
                AclResource.of(expectedParentFolder),
                null,
                null,
                now.minus(1L, ChronoUnit.HOURS),
                now.minus(5L, ChronoUnit.MINUTES),
                null);

        final var expectedAclDomainContext = DomainEventContext.create();
        final var expectedFolderDomainContext = DomainEventContext.create();

        when(userQueryGateway.findById(expectedCreatorId))
                .thenReturn(Optional.of(expectedCreator));

        when(folderQueryGateway.findVisibleById(expectedParentFolderId, expectedCreatorId))
                .thenReturn(Optional.of(expectedParentFolder));

        when(aclQueryGateway.findByResource(AclResource.of(expectedParentFolder)))
                .thenReturn(Optional.of(expectedAcl));

        when(aclCommandGateway.create(any(Acl.class)))
                .thenAnswer(returnsFirstArg());

        when(folderCommandGateway.create(any(Folder.class)))
                .thenAnswer(returnsFirstArg());

        when(eventDispatcher.append(any(Acl.class)))
                .thenReturn(expectedAclDomainContext);

        when(eventDispatcher.append(any(Folder.class)))
                .thenReturn(expectedFolderDomainContext);

        doNothing()
                .when(eventDispatcher)
                .dispatch(expectedAclDomainContext, expectedFolderDomainContext);

        final var input = new CreateFolderInput(
                expectedCreatorIdValue,
                expectedParentIdValue,
                expectedNameValue);

        final var output = useCase.execute(input);

        assertNotNull(output.id());

        verify(userQueryGateway, times(1)).findById(expectedCreatorId);
        verify(folderQueryGateway, times(1)).findVisibleById(expectedParentFolderId, expectedCreatorId);
        verify(aclQueryGateway, times(1)).findByResource(AclResource.of(expectedParentFolder));
        verify(aclCommandGateway, times(1)).create(any(Acl.class));
        verify(folderCommandGateway, times(1)).create(any(Folder.class));
        verify(eventDispatcher, times(1)).append(any(Acl.class));
        verify(eventDispatcher, times(1)).append(any(Folder.class));
        verify(eventDispatcher, times(1)).dispatch(expectedAclDomainContext, expectedFolderDomainContext);

    }

}
