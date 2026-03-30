package org.osnormais.drive.api.domain.folder;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class FolderId extends Identifier<UUID> {

    private FolderId(UUID id) {
        super(id);
    }

    public static FolderId unique() {
        return new FolderId(UUID.randomUUID());
    }

    public static FolderId of(final UUID id) {
        return new FolderId(id);
    }

    @Override
    public String getStringValue() {
        return id.toString();
    }

}
