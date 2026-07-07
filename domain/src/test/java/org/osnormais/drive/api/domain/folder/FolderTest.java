package org.osnormais.drive.api.domain.folder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.folder.event.FolderCreatedEvent;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.folder.valueobject.FolderSharing;
import org.osnormais.drive.api.domain.user.UserId;

public class FolderTest {

    @Nested
    class With {

        @Test
        void givenAValidArguments_whenInstantiateUsingWith_thenShouldInstantiate() {

            final var expectedId = FolderId.unique();
            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedFolderType = FolderType.NORMAL;
            final var expectedParentFolder = FolderId.unique();
            final var expectedName = FolderName.of("Test Folder");
            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Instant expectedDeletedAt = null;
            final Set<FolderSharing> expectedSharings = Set.of();
            final Queue<DomainEvent<?>> expectedEvents = null;

            final Folder actualFolder = Folder.with(
                    expectedId,
                    expectedCreator,
                    expectedOwner,
                    expectedFolderType,
                    expectedParentFolder,
                    expectedName,
                    expectedCreatedAt,
                    expectedUpdatedAt,
                    expectedDeletedAt,
                    expectedSharings,
                    expectedEvents);

            assertNotNull(actualFolder);
            assertEquals(expectedId, actualFolder.getId());
            assertEquals(expectedCreator, actualFolder.getCreator());
            assertEquals(expectedOwner, actualFolder.getOwner());
            assertEquals(expectedFolderType, actualFolder.getType());
            assertTrue(actualFolder.getParentFolder().isPresent());
            assertEquals(expectedParentFolder, actualFolder.getParentFolder().get());
            assertEquals(expectedName, actualFolder.getName());
            assertEquals(expectedCreatedAt, actualFolder.getCreatedAt());
            assertEquals(expectedUpdatedAt, actualFolder.getUpdatedAt());
            assertEquals(expectedDeletedAt, actualFolder.getDeletedAt());
            assertEquals(expectedSharings, actualFolder.getSharings());

        }

        @Test
        void givenAnInvalidNullArguments_whenInstantiateUsingWith_thenShouldThrowsValidationException() {

            final var expectedExceptionMessage = "'Folder' validation failed";
            final var expectedExpcectedErrorsCount = 4;

            final var expectedErrorMessage0 = "'Folder.creator' should not be null.";
            final var expectedErrorMessage1 = "'Folder.owner' should not be null.";
            final var expectedErrorMessage2 = "'Folder.name' should not be null.";
            final var expectedErrorMessage3 = "'Folder.type' should not be null.";

            final FolderId expectedId = FolderId.unique();
            final UserId expectedCreator = null;
            final UserId expectedOwner = null;
            final FolderType expectedFolderType = null;
            final FolderId expectedParentFolder = null;
            final FolderName expectedName = null;
            final Instant expectedCreatedAt = null;
            final Instant expectedUpdatedAt = null;
            final Instant expectedDeletedAt = null;
            final Set<FolderSharing> expectedSharings = null;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var actualException = assertThrows(
                    ValidationException.class,
                    () -> Folder.with(
                            expectedId,
                            expectedCreator,
                            expectedOwner,
                            expectedFolderType,
                            expectedParentFolder,
                            expectedName,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedDeletedAt,
                            expectedSharings,
                            expectedEvents));

            assertEquals(expectedExceptionMessage, actualException.getMessage());
            assertEquals(expectedExpcectedErrorsCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());
            assertEquals(expectedErrorMessage1, actualException.getErrors().get(1).message());
            assertEquals(expectedErrorMessage2, actualException.getErrors().get(2).message());
            assertEquals(expectedErrorMessage3, actualException.getErrors().get(3).message());

        }

    }

    @Nested
    class Create {

        @Test
        void givenValidArguments_whenCallsCreate_thenShouldReturnFolder() {

            final var twoHoursAgo = Instant.now().minus(2L, ChronoUnit.HOURS);

            final var expectedCreatorId = UserId.unique();
            final Folder expectedParentFolder = Folder.with(
                    FolderId.unique(),
                    UserId.unique(),
                    UserId.unique(),
                    FolderType.NORMAL,
                    null,
                    FolderName.of("Parent Folder"),
                    twoHoursAgo,
                    twoHoursAgo.plus(1l, ChronoUnit.HOURS),
                    null,
                    Set.of(FolderSharing.create(expectedCreatorId, expectedCreatorId, FolderId.unique())),
                    null);
            final var expectedName = FolderName.of("Test Folder");

            final Folder actualFolder = Folder.create(expectedCreatorId, expectedParentFolder, expectedName);

            assertNotNull(actualFolder);
            assertNotNull(actualFolder.getId());
            assertNotEquals(expectedParentFolder.getId(), actualFolder.getId());
            assertEquals(expectedName, actualFolder.getName());
            assertEquals(expectedCreatorId, actualFolder.getCreator());
            assertEquals(expectedParentFolder.getOwner(), actualFolder.getOwner());
            assertEquals(expectedParentFolder.getId(), actualFolder.getParentFolder().orElse(null));
            assertNotNull(actualFolder.getCreatedAt());
            assertNotNull(actualFolder.getUpdatedAt());
            assertEquals(actualFolder.getCreatedAt(), actualFolder.getUpdatedAt());
            assertNull(actualFolder.getDeletedAt());
            assertNotNull(actualFolder.getSharings());
            assertTrue(actualFolder.getSharings().isEmpty());

            assertNotEquals(expectedParentFolder.getName(), actualFolder.getName());
            assertNotEquals(expectedParentFolder.getCreatedAt(), actualFolder.getCreatedAt());
            assertNotEquals(expectedParentFolder.getUpdatedAt(), actualFolder.getUpdatedAt());

            assertTrue(actualFolder.getCreatedAt().isAfter(expectedParentFolder.getCreatedAt()));
            assertTrue(actualFolder.getCreatedAt().isAfter(expectedParentFolder.getUpdatedAt()));

            final var actualEvent0 = assertDoesNotThrow(() -> actualFolder.nextEvent().get());

            assertTrue(actualEvent0 instanceof FolderCreatedEvent);
            final var actualFolderCreatedEvent = (FolderCreatedEvent) actualEvent0;
            assertEquals(actualFolder.getId(), actualFolderCreatedEvent.getIdentifier());

        }

    }

}
