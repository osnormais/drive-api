package org.osnormais.drive.api.domain.transferchannel;

import java.util.UUID;

import org.osnormais.drive.api.domain.Identifier;

public class TransferChannelId extends Identifier<UUID> {

    private TransferChannelId() {
    }

    private TransferChannelId(final UUID id) {
        super(id);
    }

    public static TransferChannelId unique() {
        return new TransferChannelId(UUID.randomUUID());
    }

    public static TransferChannelId of(final UUID id) {
        return new TransferChannelId(id);
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }
}
