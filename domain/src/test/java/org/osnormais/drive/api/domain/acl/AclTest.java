package org.osnormais.drive.api.domain.acl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.osnormais.drive.api.domain.acl.event.AclCreatedEvent;
import org.osnormais.drive.api.domain.acl.valueobject.AclEntry;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.exception.AccessDeniedException;
import org.osnormais.drive.api.domain.exception.InvalidArgumentException;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

public class AclTest {

    @Nested
    class With {

        @Test
        void givenAValidArguments_whenInstantiateUsingWith_thenShouldInstantiate() {

            final var userId = UserId.unique();

            final var expectedAclId = AclId.unique();
            final var expectedResource = AclResource.create(FileId.unique(), userId);
            final var expectedDirectEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedInheritedEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var actualAcl = assertDoesNotThrow(
                    () -> Acl.with(
                            expectedAclId,
                            expectedResource,
                            expectedDirectEntries,
                            expectedInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            assertEquals(expectedAclId, actualAcl.getId());
            assertEquals(expectedResource, actualAcl.getResource());
            assertEquals(expectedDirectEntries, actualAcl.getDirectEntries());
            assertEquals(expectedInheritedEntries, actualAcl.getInheritedEntries());
            assertEquals(expectedCreatedAt, actualAcl.getCreatedAt());
            assertEquals(expectedUpdatedAt, actualAcl.getUpdatedAt());

        }

        @Test
        void givenAnInvalidNullArguments_whenInstantiateUsingWith_thenShouldThrowsValidationException() {

            final var expectedExceptionMessage = "'Acl' validation failed";
            final var expectedExpcectedErrorsCount = 3;

            final var expectedErrorMessage0 = "'Acl.resource' should not be null.";
            final var expectedErrorMessage1 = "'Acl.createdAt' should not be null.";
            final var expectedErrorMessage2 = "'Acl.updatedAt' should not be null.";

            final var expectedAclId = AclId.unique();
            final AclResource<?> expectedResource = null;
            final Set<AclEntry> expectedDirectEntries = null;
            final Set<AclEntry> expectedInheritedEntries = null;
            final Instant expectedCreatedAt = null;
            final Instant expectedUpdatedAt = null;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var actualException = assertThrows(
                    ValidationException.class,
                    () -> Acl.with(
                            expectedAclId,
                            expectedResource,
                            expectedDirectEntries,
                            expectedInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            assertEquals(expectedExceptionMessage, actualException.getMessage());
            assertEquals(expectedExpcectedErrorsCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());
            assertEquals(expectedErrorMessage1, actualException.getErrors().get(1).message());
            assertEquals(expectedErrorMessage2, actualException.getErrors().get(2).message());

        }

    }

    @Nested
    class Create {

        @Test
        void givenAValidResource_whenCreate_thenShouldCreateAclAndAppendCreatedEvent() {

            final var resource = AclResource.create(FileId.unique(), UserId.unique());

            final var actualAcl = assertDoesNotThrow(() -> Acl.create(resource));

            assertNotNull(actualAcl.getId().getValue());
            assertEquals(resource, actualAcl.getResource());
            assertEquals(Set.of(), actualAcl.getDirectEntries());
            assertEquals(Set.of(), actualAcl.getInheritedEntries());
            assertNotNull(actualAcl.getCreatedAt());
            assertNotNull(actualAcl.getUpdatedAt());

            final var actualEvent = assertDoesNotThrow(
                    () -> actualAcl.nextEvent().orElseThrow(),
                    "Expected a domain event to be appended after creating an ACL");

            assertEquals(AclCreatedEvent.class, actualEvent.getClass());
            assertEquals(actualAcl.getId(), actualEvent.getIdentifier());

        }

    }

    @Nested
    class RequiredPermission {

        @Test
        void givenAOwnerUserWithRequiredPermission_whenRequiredPermission_thenShouldNotThrow() {

            final var ownerUserId = UserId.unique();

            final var expectedAclId = AclId.unique();
            final var expectedResource = AclResource.create(FileId.unique(), ownerUserId);
            final var expectedDirectEntries = Set.<AclEntry>of();
            final var expectedInheritedEntries = Set.<AclEntry>of();
            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var acl = assertDoesNotThrow(
                    () -> Acl.with(
                            expectedAclId,
                            expectedResource,
                            expectedDirectEntries,
                            expectedInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            assertDoesNotThrow(() -> acl.requiredPermission(ownerUserId, Permission.OWNER));

        }

        @Test
        void givenAUserWithRequiredPermission_whenRequiredPermission_thenShouldNotThrow() {

            final var ownerUserId = UserId.unique();
            final var userId = UserId.unique();

            final var expectedAclId = AclId.unique();
            final var expectedResource = AclResource.create(FileId.unique(), ownerUserId);
            final var expectedDirectEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedInheritedEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var acl = assertDoesNotThrow(
                    () -> Acl.with(
                            expectedAclId,
                            expectedResource,
                            expectedDirectEntries,
                            expectedInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            assertDoesNotThrow(() -> acl.requiredPermission(userId, Permission.READ));

        }

        @Test
        void givenAUserWithoutRequiredPermission_whenRequiredPermission_thenShouldThrowsAccessDeniedException() {

            final var ownerUserId = UserId.unique();
            final var userId = UserId.unique();

            final var expectedAclId = AclId.unique();
            final var expectedResource = AclResource.create(FileId.unique(), ownerUserId);
            final var expectedDirectEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedInheritedEntries = Set.of(AclEntry.grant(userId, Permission.READ));
            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var expectedExceptionMessage = "User [%s] does not have required permission [%s] for resource [%s]"
                    .formatted(
                            userId.getStringValue(),
                            Permission.WRITE,
                            expectedResource.resourceType() + ":" + expectedResource.resourceId().getStringValue());
            final var expectedExpcectedErrorsCount = 1;
            final var expectedErrorMessage0 = expectedExceptionMessage;

            final var acl = assertDoesNotThrow(
                    () -> Acl.with(
                            expectedAclId,
                            expectedResource,
                            expectedDirectEntries,
                            expectedInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            final var actualException = assertThrows(
                    AccessDeniedException.class,
                    () -> acl.requiredPermission(userId, Permission.WRITE));

            assertEquals(expectedExceptionMessage, actualException.getMessage());
            assertEquals(expectedExpcectedErrorsCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());

        }

    }

    @Nested
    class DeriveFor {

        @Test
        void givenASameResource_whenDeriveFor_thenShouldNotInheritEntries() {

            final var ownerUserId = UserId.unique();

            final var anotherUserId0 = UserId.unique();
            final var anotherUserId1 = UserId.unique();

            final var originalAclId = AclId.unique();

            final var originalResourceId = FolderId.unique();

            final var originalResource = AclResource.create(originalResourceId, ownerUserId);
            final var expectedResource = AclResource.create(originalResourceId, ownerUserId);

            final var originalDirectEntries = Set.of(
                    AclEntry.grant(anotherUserId0, Permission.READ),
                    AclEntry.grant(anotherUserId1, Permission.READ));
            final var originalInheritedEntries = Set.of(AclEntry.grant(anotherUserId0, Permission.WRITE));

            final Set<AclEntry> expectedDirectEntries = originalDirectEntries;
            final var expectedInheritedEntries = originalInheritedEntries;

            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var originalAcl = assertDoesNotThrow(
                    () -> Acl.with(
                            originalAclId,
                            originalResource,
                            originalDirectEntries,
                            originalInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            final var actualAcl = assertDoesNotThrow(() -> originalAcl.deriveFor(expectedResource));

            assertNotNull(actualAcl.getId().getValue());
            assertEquals(originalAclId, actualAcl.getId());
            assertEquals(expectedResource, actualAcl.getResource());
            assertEquals(expectedDirectEntries, actualAcl.getDirectEntries());
            assertEquals(expectedInheritedEntries, actualAcl.getInheritedEntries());
            assertNotNull(actualAcl.getCreatedAt());
            assertNotNull(actualAcl.getUpdatedAt());

            assertTrue(actualAcl.nextEvent().isEmpty());

        }

        @Test
        void givenAValidResource_whenDeriveFor_thenShouldDeriveAclForTheSpecifiedResource() {

            final var ownerUserId = UserId.unique();

            final var anotherUserId0 = UserId.unique();
            final var anotherUserId1 = UserId.unique();

            final var originalAclId = AclId.unique();
            final var originalResource = AclResource.create(FolderId.unique(), ownerUserId);
            final var expectedResource = AclResource.create(FileId.unique(), ownerUserId);
            final var originalDirectEntries = Set.of(
                    AclEntry.grant(anotherUserId0, Permission.READ),
                    AclEntry.grant(anotherUserId1, Permission.READ));
            final var originalInheritedEntries = Set.of(AclEntry.grant(anotherUserId0, Permission.WRITE));

            final Set<AclEntry> expectedDirectEntries = Set.of();
            final var expectedInheritedEntries = Stream
                    .concat(originalDirectEntries.stream(), originalInheritedEntries.stream())
                    .collect(Collectors.toSet());

            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var originalAcl = assertDoesNotThrow(
                    () -> Acl.with(
                            originalAclId,
                            originalResource,
                            originalDirectEntries,
                            originalInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            final var actualAcl = assertDoesNotThrow(() -> originalAcl.deriveFor(expectedResource));

            assertNotNull(actualAcl.getId().getValue());
            assertNotEquals(originalAclId, actualAcl.getId());
            assertEquals(expectedResource, actualAcl.getResource());
            assertEquals(expectedDirectEntries, actualAcl.getDirectEntries());
            assertEquals(expectedInheritedEntries, actualAcl.getInheritedEntries());
            assertNotNull(actualAcl.getCreatedAt());
            assertNotNull(actualAcl.getUpdatedAt());

            final var actualEvent0 = assertDoesNotThrow(() -> actualAcl.nextEvent().get(),
                    "Expected no domain event to be appended after deriving an ACL");

            assertEquals(AclCreatedEvent.class, actualEvent0.getClass());
            assertEquals(actualAcl.getId(), actualEvent0.getIdentifier());

        }

        @Test
        void givenAValidResourceButDifferentOwner_whenDeriveFor_thenShouldThrowsInvalidArgumentException() {

            final var ownerUserId = UserId.unique();

            final var anotherUserId0 = UserId.unique();
            final var anotherUserId1 = UserId.unique();

            final var originalAclId = AclId.unique();
            final var originalResource = AclResource.create(FolderId.unique(), ownerUserId);
            final var expectedResource = AclResource.create(FileId.unique(), anotherUserId0);
            final var originalDirectEntries = Set.of(
                    AclEntry.grant(anotherUserId0, Permission.READ),
                    AclEntry.grant(anotherUserId1, Permission.READ));
            final var originalInheritedEntries = Set.of(AclEntry.grant(anotherUserId0, Permission.WRITE));

            final var expectedCreatedAt = Instant.now();
            final var expectedUpdatedAt = expectedCreatedAt;
            final Queue<DomainEvent<?>> expectedEvents = null;

            final var expectedExceptionMessage = "Invalid argument provided.";
            final var expectedExpcectedErrorsCount = 1;

            final var expectedErrorMessage0 = "'resource' should belong to the same owner as the current ACL.";

            final var originalAcl = assertDoesNotThrow(
                    () -> Acl.with(
                            originalAclId,
                            originalResource,
                            originalDirectEntries,
                            originalInheritedEntries,
                            expectedCreatedAt,
                            expectedUpdatedAt,
                            expectedEvents));

            final var actualException = assertThrowsExactly(InvalidArgumentException.class,
                    () -> originalAcl.deriveFor(expectedResource));

            assertEquals(expectedExceptionMessage, actualException.getMessage());
            assertEquals(expectedExpcectedErrorsCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage0, actualException.getErrors().get(0).message());

        }

    }

}
