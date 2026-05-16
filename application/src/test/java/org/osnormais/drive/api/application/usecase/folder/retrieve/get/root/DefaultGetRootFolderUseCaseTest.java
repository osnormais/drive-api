package org.osnormais.drive.api.application.usecase.folder.retrieve.get.root;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.domain.event.DomainEventContext;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.event.DomainEventSource;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

@ExtendWith(MockitoExtension.class)
public class DefaultGetRootFolderUseCaseTest {

    @InjectMocks
    DefaultGetRootFolderUseCase useCase;

    @Mock
    UserQueryGateway userQueryGateway;

    @Mock
    FileQueryGateway fileQueryGateway;

    @Mock
    FolderQueryGateway folderQueryGateway;

    @Mock
    FolderCommandGateway folderCommandGateway;

    @Mock
    AclCommandGateway aclCommandGateway;

    @Mock
    DomainEventDispatcher domainEventDispatcher;

    @Test
    void givenValidInput_whenRootFolderDoesNotExist_thenCreatesRootFolder() {

        final var expectedOwnerIdvalue = UUID.randomUUID();
        final var expectedOwnerId = UserId.of(expectedOwnerIdvalue);

        final var expectedOwner = User.with(
                expectedOwnerId,
                PlanId.unique(),
                null,
                null);

        final var expectedRootFolderCreator = expectedOwnerId;
        final var expectedRootFolderOwner = expectedOwnerId;
        final var expectedRootFolderParent = Optional.<FolderId>empty();
        final var expectedRootFolderName = FolderName.of("root");

        when(userQueryGateway.findById(expectedOwnerId))
                .thenReturn(Optional.of(expectedOwner));

        when(folderQueryGateway.findByOwnerAndType(expectedOwnerId, FolderType.ROOT))
                .thenReturn(Optional.empty());

        when(aclCommandGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        when(folderCommandGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        when(folderQueryGateway.findAllByParent(any()))
                .thenReturn(Set.of());

        when(fileQueryGateway.findAllByFolder(any()))
                .thenReturn(Set.of());

        final var input = new GetRootFolderInput(expectedOwnerIdvalue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertNotNull(actualOutput.id());
        assertEquals(expectedRootFolderName.value(), actualOutput.name());
        assertNotNull(actualOutput.subFolders());
        assertTrue(actualOutput.subFolders().isEmpty());
        assertNotNull(actualOutput.files());
        assertTrue(actualOutput.files().isEmpty());
        assertEquals(expectedOwnerIdvalue, actualOutput.ownerId());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());

        verify(userQueryGateway, times(1)).findById(any());
        verify(userQueryGateway, times(1)).findById(expectedOwnerId);

        verify(folderQueryGateway, times(1)).findByOwnerAndType(any(), any());
        verify(folderQueryGateway, times(1)).findByOwnerAndType(expectedOwnerId, FolderType.ROOT);

        verify(aclCommandGateway, times(1)).create(any());
        verify(folderCommandGateway, times(1)).create(any());
        verify(folderCommandGateway, times(1)).create(argThat(rootFolder -> {

            assertEquals(expectedRootFolderCreator, rootFolder.getCreator());
            assertEquals(expectedRootFolderOwner, rootFolder.getOwner());
            assertEquals(expectedRootFolderParent, rootFolder.getParentFolder());
            assertEquals(expectedRootFolderName, rootFolder.getName());

            return true;
        }));

        verify(domainEventDispatcher, times(2)).append(any(DomainEventSource.class));
        verify(domainEventDispatcher, times(1)).append(any(Acl.class));
        verify(domainEventDispatcher, times(1)).append(any(Folder.class));

        verify(folderQueryGateway, times(1)).findAllByParent(any());
        verify(fileQueryGateway, times(1)).findAllByFolder(any());

        verify(domainEventDispatcher, times(1)).dispatch(any(DomainEventContext[].class));

    }

    @Test
    void givenValidInput_whenRootFolderExists_thenShouldRetrieveRootFolder() {

        final var expectedOwnerIdvalue = UUID.randomUUID();
        final var expectedOwnerId = UserId.of(expectedOwnerIdvalue);

        final var expectedOwner = User.with(
                expectedOwnerId,
                PlanId.unique(),
                null,
                null);

        final var now = Instant.now();

        final var expectedRootFolderIdValue = UUID.randomUUID();
        final var expectedRootFolderId = FolderId.of(expectedRootFolderIdValue);
        final var expectedRootFolderCreator = expectedOwnerId;
        final var expectedRootFolderOwner = expectedOwnerId;
        final var expectedRootFolderName = FolderName.of("root");

        final var expectedRootFolder = Folder.with(
                expectedRootFolderId,
                expectedRootFolderCreator,
                expectedRootFolderOwner,
                FolderType.ROOT,
                null,
                expectedRootFolderName,
                now.minus(2, ChronoUnit.DAYS),
                now.minus(1, ChronoUnit.HOURS),
                null,
                null,
                null);

        final var expectedSubFolder0 = Folder.with(
                FolderId.unique(),
                expectedRootFolderCreator,
                expectedRootFolderOwner,
                FolderType.NORMAL,
                expectedRootFolderId,
                FolderName.of("subFolder0"),
                now,
                now,
                null,
                null,
                null);

        final var expectedFile0 = File.with(
                FileId.unique(),
                expectedRootFolderCreator,
                expectedRootFolderOwner,
                expectedRootFolderId,
                FileName.of("file0"),
                Checksum.of(Checksum.Algorithm.SHA_256, "SHA256"),
                FileSize.of(1024L),
                Content.of("file"),
                now,
                now,
                null,
                null,
                null);

        when(userQueryGateway.findById(expectedOwnerId))
                .thenReturn(Optional.of(expectedOwner));

        when(folderQueryGateway.findByOwnerAndType(expectedOwnerId, FolderType.ROOT))
                .thenReturn(Optional.of(expectedRootFolder));

        when(folderQueryGateway.findAllByParent(any()))
                .thenReturn(Set.of(expectedSubFolder0));

        when(fileQueryGateway.findAllByFolder(any()))
                .thenReturn(Set.of(expectedFile0));

        final var input = new GetRootFolderInput(expectedOwnerIdvalue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertNotNull(actualOutput.id());
        assertEquals(expectedRootFolderName.value(), actualOutput.name());
        assertNotNull(actualOutput.subFolders());
        assertEquals(expectedSubFolder0.getId().getValue(), actualOutput.subFolders().get(0).id());
        assertEquals(expectedSubFolder0.getName().value(), actualOutput.subFolders().get(0).name());
        assertNotNull(actualOutput.files());
        assertEquals(expectedFile0.getId().getValue(), actualOutput.files().get(0).id());
        assertEquals(expectedFile0.getName().value(), actualOutput.files().get(0).name());
        assertEquals(expectedFile0.getContent().type(), actualOutput.files().get(0).contentType());
        assertEquals(expectedOwnerIdvalue, actualOutput.ownerId());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());

        verify(userQueryGateway, times(1)).findById(any());
        verify(userQueryGateway, times(1)).findById(expectedOwnerId);

        verify(folderQueryGateway, times(1)).findByOwnerAndType(any(), any());
        verify(folderQueryGateway, times(1)).findByOwnerAndType(expectedOwnerId, FolderType.ROOT);

        verify(folderQueryGateway, times(1)).findAllByParent(any());
        verify(fileQueryGateway, times(1)).findAllByFolder(any());

        verify(folderCommandGateway, times(0)).create(any());
        verify(aclCommandGateway, times(0)).create(any());
        verify(domainEventDispatcher, times(0)).append(any(DomainEventSource.class));
        verify(domainEventDispatcher, times(0)).dispatch(any(DomainEventContext[].class));

    }

    @Test
    void givenValidInput_whenUserDoesNotExist_thenShouldThrowsNotFOundExcpetion() {

        final var expectedOwnerIdvalue = UUID.randomUUID();
        final var expectedOwnerId = UserId.of(expectedOwnerIdvalue);

        final var expectedExcpetionMessage = "[User] not found";
        final var expectedExcpetionErrorsCount = 1;
        final var expectedExcpetionError0 = "[User] with id [%s] not found".formatted(expectedOwnerIdvalue);

        when(userQueryGateway.findById(expectedOwnerId))
                .thenReturn(Optional.empty());

        final var input = new GetRootFolderInput(expectedOwnerIdvalue);

        final var actualException = assertThrowsExactly(NotFoundException.class, () -> useCase.execute(input));

        assertNotNull(actualException);
        assertEquals(expectedExcpetionMessage, actualException.getMessage());
        assertEquals(expectedExcpetionErrorsCount, actualException.getErrors().size());
        assertEquals(expectedExcpetionError0, actualException.getErrors().get(0).message());

    }

}
