package org.osnormais.drive.api.application.usecase.folder.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.UserId;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFolderUseCaseTest {

    @InjectMocks
    DefaultGetFolderUseCase useCase;

    @Mock
    FolderQueryGateway folderQueryGateway;

    @Mock
    FileQueryGateway fileQueryGateway;

    @Test
    void givenValidInput_whenFolderExistsWithChildren_thenShouldRetrieveFolder() {

        final var expectedFolderIdValue = UUID.randomUUID();
        final var expectedFolderId = FolderId.of(expectedFolderIdValue);
        final var expectedUserIdValue = UUID.randomUUID();
        final var expectedUserId = UserId.of(expectedUserIdValue);
        final var expectedOwnerId = UserId.of(UUID.randomUUID());

        final var now = Instant.now();

        final var expectedParentFolderId = FolderId.unique();

        final var expectedFolder = Folder.with(
                expectedFolderId,
                expectedOwnerId,
                expectedOwnerId,
                expectedParentFolderId,
                FolderName.of("My Folder"),
                now.minus(2, ChronoUnit.DAYS),
                now.minus(1, ChronoUnit.HOURS),
                null,
                null,
                null);

        final var expectedSubFolder0 = Folder.with(
                FolderId.unique(),
                expectedOwnerId,
                expectedOwnerId,
                expectedFolderId,
                FolderName.of("subFolder0"),
                now,
                now,
                null,
                null,
                null);

        final var expectedFile0 = File.with(
                FileId.unique(),
                expectedOwnerId,
                expectedOwnerId,
                expectedFolderId,
                FileName.of("file0.txt"),
                Checksum.of(Checksum.Algorithm.SHA_256, "SHA256HASH"),
                Size.of(2048L),
                Content.of("text/plain"),
                now,
                now,
                null,
                null,
                null);

        when(folderQueryGateway.findVisibleById(expectedFolderId, expectedUserId))
                .thenReturn(Optional.of(expectedFolder));

        when(folderQueryGateway.findAllByParent(expectedFolderId))
                .thenReturn(Set.of(expectedSubFolder0));

        when(fileQueryGateway.findAllByFolder(expectedFolderId))
                .thenReturn(Set.of(expectedFile0));

        final var input = new GetFolderInput(expectedFolderIdValue, expectedUserIdValue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertNotNull(actualOutput.id());
        assertEquals(expectedFolderIdValue, actualOutput.id());
        assertEquals("My Folder", actualOutput.name());
        assertFalse(actualOutput.isRoot());
        assertEquals(expectedParentFolderId.getValue(), actualOutput.parentId());
        assertNotNull(actualOutput.subFolders());
        assertEquals(1, actualOutput.subFolders().size());
        assertEquals(expectedSubFolder0.getId().getValue(), actualOutput.subFolders().get(0).id());
        assertEquals(expectedSubFolder0.getName().value(), actualOutput.subFolders().get(0).name());
        assertNotNull(actualOutput.files());
        assertEquals(1, actualOutput.files().size());
        assertEquals(expectedFile0.getId().getValue(), actualOutput.files().get(0).id());
        assertEquals(expectedFile0.getName().value(), actualOutput.files().get(0).name());
        assertEquals(expectedFile0.getSize().bytes(), actualOutput.files().get(0).sizeInBytes());
        assertEquals(expectedFile0.getContent().type(), actualOutput.files().get(0).contentType());
        assertEquals(expectedOwnerId.getValue(), actualOutput.ownerId());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());

        verify(folderQueryGateway, times(1)).findVisibleById(expectedFolderId, expectedUserId);
        verify(folderQueryGateway, times(1)).findAllByParent(expectedFolderId);
        verify(fileQueryGateway, times(1)).findAllByFolder(expectedFolderId);

    }

    @Test
    void givenValidInput_whenFolderExistsEmpty_thenShouldRetrieveFolder() {

        final var expectedFolderIdValue = UUID.randomUUID();
        final var expectedFolderId = FolderId.of(expectedFolderIdValue);
        final var expectedUserIdValue = UUID.randomUUID();
        final var expectedUserId = UserId.of(expectedUserIdValue);
        final var expectedOwnerId = UserId.of(UUID.randomUUID());

        final var now = Instant.now();

        final var expectedFolder = Folder.with(
                expectedFolderId,
                expectedOwnerId,
                expectedOwnerId,
                null,
                FolderName.of("Empty Folder"),
                now.minus(1, ChronoUnit.DAYS),
                now.minus(30, ChronoUnit.MINUTES),
                null,
                null,
                null);

        when(folderQueryGateway.findVisibleById(expectedFolderId, expectedUserId))
                .thenReturn(Optional.of(expectedFolder));

        when(folderQueryGateway.findAllByParent(expectedFolderId))
                .thenReturn(Set.of());

        when(fileQueryGateway.findAllByFolder(expectedFolderId))
                .thenReturn(Set.of());

        final var input = new GetFolderInput(expectedFolderIdValue, expectedUserIdValue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertNotNull(actualOutput.id());
        assertEquals(expectedFolderIdValue, actualOutput.id());
        assertEquals("Empty Folder", actualOutput.name());
        assertTrue(actualOutput.isRoot());
        assertEquals(null, actualOutput.parentId());
        assertNotNull(actualOutput.subFolders());
        assertTrue(actualOutput.subFolders().isEmpty());
        assertNotNull(actualOutput.files());
        assertTrue(actualOutput.files().isEmpty());
        assertEquals(expectedOwnerId.getValue(), actualOutput.ownerId());
        assertNotNull(actualOutput.createdAt());
        assertNotNull(actualOutput.updatedAt());

        verify(folderQueryGateway, times(1)).findVisibleById(expectedFolderId, expectedUserId);
        verify(folderQueryGateway, times(1)).findAllByParent(expectedFolderId);
        verify(fileQueryGateway, times(1)).findAllByFolder(expectedFolderId);

    }

    @Test
    void givenValidInput_whenFolderIsRoot_thenShouldReturnIsRootTrue() {

        final var expectedFolderIdValue = UUID.randomUUID();
        final var expectedFolderId = FolderId.of(expectedFolderIdValue);
        final var expectedUserIdValue = UUID.randomUUID();
        final var expectedUserId = UserId.of(expectedUserIdValue);
        final var expectedOwnerId = UserId.of(UUID.randomUUID());

        final var now = Instant.now();

        final var expectedFolder = Folder.with(
                expectedFolderId,
                expectedOwnerId,
                expectedOwnerId,
                null,
                FolderName.of("root"),
                now,
                now,
                null,
                null,
                null);

        when(folderQueryGateway.findVisibleById(expectedFolderId, expectedUserId))
                .thenReturn(Optional.of(expectedFolder));

        when(folderQueryGateway.findAllByParent(expectedFolderId))
                .thenReturn(Set.of());

        when(fileQueryGateway.findAllByFolder(expectedFolderId))
                .thenReturn(Set.of());

        final var input = new GetFolderInput(expectedFolderIdValue, expectedUserIdValue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertTrue(actualOutput.isRoot());
        assertEquals(null, actualOutput.parentId());

        verify(folderQueryGateway, times(1)).findVisibleById(expectedFolderId, expectedUserId);

    }

    @Test
    void givenValidInput_whenFolderIsNotRoot_thenShouldReturnIsRootFalseAndParentId() {

        final var expectedFolderIdValue = UUID.randomUUID();
        final var expectedFolderId = FolderId.of(expectedFolderIdValue);
        final var expectedUserIdValue = UUID.randomUUID();
        final var expectedUserId = UserId.of(expectedUserIdValue);
        final var expectedOwnerId = UserId.of(UUID.randomUUID());
        final var expectedParentFolderId = FolderId.unique();
        final var expectedParentFolderIdValue = expectedParentFolderId.getValue();

        final var now = Instant.now();

        final var expectedFolder = Folder.with(
                expectedFolderId,
                expectedOwnerId,
                expectedOwnerId,
                expectedParentFolderId,
                FolderName.of("Child Folder"),
                now,
                now,
                null,
                null,
                null);

        when(folderQueryGateway.findVisibleById(expectedFolderId, expectedUserId))
                .thenReturn(Optional.of(expectedFolder));

        when(folderQueryGateway.findAllByParent(expectedFolderId))
                .thenReturn(Set.of());

        when(fileQueryGateway.findAllByFolder(expectedFolderId))
                .thenReturn(Set.of());

        final var input = new GetFolderInput(expectedFolderIdValue, expectedUserIdValue);

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertFalse(actualOutput.isRoot());
        assertEquals(expectedParentFolderIdValue, actualOutput.parentId());

        verify(folderQueryGateway, times(1)).findVisibleById(expectedFolderId, expectedUserId);

    }

    @Test
    void givenValidInput_whenFolderNotFound_thenShouldThrowNotFoundException() {

        final var expectedFolderIdValue = UUID.randomUUID();
        final var expectedFolderId = FolderId.of(expectedFolderIdValue);
        final var expectedUserIdValue = UUID.randomUUID();
        final var expectedUserId = UserId.of(expectedUserIdValue);

        final var expectedExceptionMessage = "[Folder] not found";
        final var expectedExceptionErrorsCount = 1;
        final var expectedExceptionError0 = "[Folder] with id [%s] not found".formatted(expectedFolderIdValue);

        when(folderQueryGateway.findVisibleById(expectedFolderId, expectedUserId))
                .thenReturn(Optional.empty());

        final var input = new GetFolderInput(expectedFolderIdValue, expectedUserIdValue);

        final var actualException = assertThrowsExactly(NotFoundException.class, () -> useCase.execute(input));

        assertNotNull(actualException);
        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedExceptionErrorsCount, actualException.getErrors().size());
        assertEquals(expectedExceptionError0, actualException.getErrors().get(0).message());

        verify(folderQueryGateway, times(1)).findVisibleById(expectedFolderId, expectedUserId);
        verify(folderQueryGateway, times(0)).findAllByParent(any());
        verify(fileQueryGateway, times(0)).findAllByFolder(any());

    }

}
