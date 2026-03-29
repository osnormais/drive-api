package org.osnormais.drive.api.domain.file;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class FileId extends Identifier<UUID> {

    private FileId(UUID id) {
        super(id);
    }

    public static FileId unique() {
        return new FileId(UUID.randomUUID());
    }

    public static FileId of(final UUID id) {
        return new FileId(id);
    }

    @Override
    public String getStringValue() {
        return id.toString();
    }

}
