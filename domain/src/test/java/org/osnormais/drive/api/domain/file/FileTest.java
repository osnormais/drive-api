package org.osnormais.drive.api.domain.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.FileSharing;
import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

public class FileTest {

    @Nested
    class With {

        @Test
        void givenAValidArguments_whenInstantiateUsingWith_thenShouldInstantiate() {

            final var now = Instant.now().minus(1l, ChronoUnit.DAYS);

            final var expectedId = FileId.unique();
            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedFolder = FolderId.unique();
            final var expectedName = FileName.of("text.txt");
            final var expectedChecksum = Checksum.of(Checksum.Algorithm.CRC_32, "12345678");
            final var expectedSize = FileSize.of(1024L);
            final var expectedContent = Content.of("text/plain; charset=UTF-8");
            final var expectedCreatedAt = now;
            final var expectedUpdatedAt = now.plus(1l, ChronoUnit.HOURS);
            final var expectedDeletedAt = now.plus(5l, ChronoUnit.HOURS);
            final var expectedSharings = Set
                    .of(FileSharing.with(
                            UserId.unique(),
                            UserId.unique(),
                            FolderId.unique(),
                            now.plus(1L, ChronoUnit.HOURS)));
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var actualFile = assertDoesNotThrow(() -> File.with(
                    expectedId,
                    expectedCreator,
                    expectedOwner,
                    expectedFolder,
                    expectedName,
                    expectedChecksum,
                    expectedSize,
                    expectedContent,
                    expectedCreatedAt,
                    expectedUpdatedAt,
                    expectedDeletedAt,
                    expectedSharings,
                    expectedEvents));

            assertEquals(expectedId, actualFile.getId());
            assertEquals(expectedCreator, actualFile.getCreator());
            assertEquals(expectedOwner, actualFile.getOwner());
            assertEquals(expectedFolder, actualFile.getFolder());
            assertEquals(expectedName, actualFile.getName());
            assertEquals(expectedChecksum, actualFile.getChecksum());
            assertEquals(expectedSize, actualFile.getSize());
            assertEquals(expectedContent, actualFile.getContent());
            assertEquals(expectedCreatedAt, actualFile.getCreatedAt());
            assertEquals(expectedUpdatedAt, actualFile.getUpdatedAt());
            assertEquals(expectedDeletedAt, actualFile.getDeletedAt());
            assertEquals(expectedSharings, actualFile.getSharings());

        }

        @Test
        void givenAnInvalidArguments_whenInstantiateUsingWith_thenShouldThrowsValidationException() {

            final FileId expectedId = FileId.unique();
            final UserId expectedCreator = null;
            final UserId expectedOwner = null;
            final FolderId expectedFolder = null;
            final FileName expectedName = null;
            final Checksum expectedChecksum = null;
            final FileSize expectedSize = null;
            final Content expectedContent = null;
            final Instant expectedCreatedAt = null;
            final Instant expectedUpdatedAt = null;
            final Instant expectedDeletedAt = null;
            final Set<FileSharing> expectedSharings = null;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var expectedExceptionMessage = "'File' validation failed";

            final var expectedErrorsCount = 6;
            final var expectedErrorMessage0 = "'File.checksum' should not be null";
            final var expectedErrorMessage1 = "'File.size' should not be null";
            final var expectedErrorMessage2 = "'File.name' should not be null";
            final var expectedErrorMessage3 = "'File.content' should not be null";
            final var expectedErrorMessage4 = "'File.createdAt' should not be null";
            final var expectedErrorMessage5 = "'File.updatedAt' should not be null";

            final var actualException = assertThrows(ValidationException.class, () -> File.with(
                    expectedId,
                    expectedCreator,
                    expectedOwner,
                    expectedFolder,
                    expectedName,
                    expectedChecksum,
                    expectedSize,
                    expectedContent,
                    expectedCreatedAt,
                    expectedUpdatedAt,
                    expectedDeletedAt,
                    expectedSharings,
                    expectedEvents));

            assertEquals(expectedExceptionMessage, actualException.getMessage());
            assertEquals(expectedErrorsCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());
            assertEquals(expectedErrorMessage1, actualException.getErrors().get(1).message());
            assertEquals(expectedErrorMessage2, actualException.getErrors().get(2).message());
            assertEquals(expectedErrorMessage3, actualException.getErrors().get(3).message());
            assertEquals(expectedErrorMessage4, actualException.getErrors().get(4).message());
            assertEquals(expectedErrorMessage5, actualException.getErrors().get(5).message());

        }

    }

    @Nested
    class Create {

        @Test
        void givenAValidArguments_whenInstantiateUsingCreate_thenShouldInstantiateAndAddCreatedEvent() {

            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedFolder = FolderId.unique();
            final var expectedName = FileName.of("text.txt");
            final var expectedChecksum = Checksum.of(Checksum.Algorithm.CRC_32, "12345678");
            final var expectedSize = FileSize.of(1024L);
            final var expectedContent = Content.of("text/plain; charset=UTF-8");

            final var actualFile = assertDoesNotThrow(() -> File.create(
                    expectedCreator,
                    expectedOwner,
                    expectedFolder,
                    expectedName,
                    expectedChecksum,
                    expectedSize,
                    expectedContent));

            assertEquals(expectedCreator, actualFile.getCreator());
            assertEquals(expectedOwner, actualFile.getOwner());
            assertEquals(expectedFolder, actualFile.getFolder());
            assertEquals(expectedName, actualFile.getName());
            assertEquals(expectedChecksum, actualFile.getChecksum());
            assertEquals(expectedSize, actualFile.getSize());
            assertEquals(expectedContent, actualFile.getContent());
            assertEquals(actualFile.getCreatedAt(), actualFile.getUpdatedAt());
            assertNull(actualFile.getDeletedAt());
            assertTrue(actualFile.getSharings().isEmpty());

            final var actualEvent = assertDoesNotThrow(() -> actualFile.nextEvent().get());
            assertTrue(actualEvent instanceof FileCreatedEvent);

            assertEquals(actualFile.getId(), actualEvent.getIdentifier());

        }

    }

    @Nested
    class GetFolderFor {

        @Test
        void givenOwnerUser_whenCallsGetFolderFor_thenShouldReturnActualFolder() {

            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedFolder = FolderId.unique();
            final var now = Instant.now();

            final var file = File.with(
                    FileId.unique(),
                    expectedCreator,
                    expectedOwner,
                    expectedFolder,
                    FileName.of("file.txt"),
                    Checksum.of(Checksum.Algorithm.CRC_32, "ABC123"),
                    FileSize.of(512L),
                    Content.of("text/plain"),
                    now,
                    now,
                    null,
                    Set.of(),
                    null);

            final var actualFolder = file.getFolderFor(expectedOwner);

            assertEquals(expectedFolder, actualFolder);

        }

        @Test
        void givenSharedUser_whenCallsGetFolderFor_thenShouldReturnVirtualFolder() {

            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedSharedUser = UserId.unique();
            final var expectedActualFolder = FolderId.unique();
            final var expectedVirtualFolder = FolderId.unique();
            final var now = Instant.now();

            final var file = File.with(
                    FileId.unique(),
                    expectedCreator,
                    expectedOwner,
                    expectedActualFolder,
                    FileName.of("file.txt"),
                    Checksum.of(Checksum.Algorithm.CRC_32, "ABC123"),
                    FileSize.of(512L),
                    Content.of("text/plain"),
                    now,
                    now,
                    null,
                    Set.of(FileSharing.with(
                            expectedSharedUser,
                            expectedOwner,
                            expectedVirtualFolder,
                            now)),
                    null);

            final var actualFolder = file.getFolderFor(expectedSharedUser);

            assertEquals(expectedVirtualFolder, actualFolder);

        }

        @Test
        void givenUnrelatedUser_whenCallsGetFolderFor_thenShouldReturnActualFolder() {

            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedUnrelatedUser = UserId.unique();
            final var expectedActualFolder = FolderId.unique();
            final var expectedVirtualFolder = FolderId.unique();
            final var now = Instant.now();

            final var file = File.with(
                    FileId.unique(),
                    expectedCreator,
                    expectedOwner,
                    expectedActualFolder,
                    FileName.of("file.txt"),
                    Checksum.of(Checksum.Algorithm.CRC_32, "ABC123"),
                    FileSize.of(512L),
                    Content.of("text/plain"),
                    now,
                    now,
                    null,
                    Set.of(FileSharing.with(
                            UserId.unique(),
                            expectedOwner,
                            expectedVirtualFolder,
                            now)),
                    null);

            final var actualFolder = file.getFolderFor(expectedUnrelatedUser);

            assertEquals(expectedActualFolder, actualFolder);

        }

        @Test
        void givenEmptySharings_whenCallsGetFolderFor_thenShouldReturnActualFolder() {

            final var expectedCreator = UserId.unique();
            final var expectedOwner = UserId.unique();
            final var expectedSharedUser = UserId.unique();
            final var expectedActualFolder = FolderId.unique();
            final var now = Instant.now();

            final var file = File.with(
                    FileId.unique(),
                    expectedCreator,
                    expectedOwner,
                    expectedActualFolder,
                    FileName.of("file.txt"),
                    Checksum.of(Checksum.Algorithm.CRC_32, "ABC123"),
                    FileSize.of(512L),
                    Content.of("text/plain"),
                    now,
                    now,
                    null,
                    Set.of(),
                    null);

            final var actualFolder = file.getFolderFor(expectedSharedUser);

            assertEquals(expectedActualFolder, actualFolder);

        }

    }

}
