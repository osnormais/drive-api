package org.osnormais.drive.api.domain.folder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.osnormais.drive.api.domain.entitlement.grant.GrantId;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.domain.exception.FolderAlreadyExistsException;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class FolderCreationServiceTest {

    @Test
    void givenNoSiblingsWithSameName_whenCreateFolder_thenReturnFolder() {

        final Boolean hasSiblingsWithSameName = false;
        final User creator = User.with(
                UserId.unique(),
                PlanId.unique(),
                GrantId.unique(),
                null);
        final Folder parentFolder = Folder.createRoot(creator.getId());
        final FolderName name = FolderName.of("folder name");

        final var actualFolder = FolderCreationService.createFolder(
                hasSiblingsWithSameName,
                creator,
                parentFolder,
                name);

        assertEquals(name, actualFolder.getName());
        assertEquals(parentFolder.getId(), actualFolder.getParentFolder().get());
        assertEquals(creator.getId(), actualFolder.getOwner());

    }

    @Test
    void givenHasSiblingsWithSameName_whenCreateFolder_thenFolderAlreadyExistsException() {

        final Boolean hasSiblingsWithSameName = true;
        final User creator = User.with(
                UserId.unique(),
                PlanId.unique(),
                GrantId.unique(),
                null);
        final Folder parentFolder = Folder.createRoot(creator.getId());
        final FolderName name = FolderName.of("folder name");

        final var expectedExceptionMessage = "Folder name [folder name] already exists.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "A folder with the same name already exists in the target folder. Please choose a different name or remove the existing folder.";

        final var actualException = assertThrowsExactly(
                FolderAlreadyExistsException.class,
                () -> FolderCreationService.createFolder(
                        hasSiblingsWithSameName,
                        creator,
                        parentFolder,
                        name));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

}
