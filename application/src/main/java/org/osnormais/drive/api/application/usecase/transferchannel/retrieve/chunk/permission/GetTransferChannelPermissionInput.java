package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission;

import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.application.exception.InvalidInputException;

public record GetTransferChannelPermissionInput(
        UUID actorId,
        UUID transferChannelId,
        Set<Range> ranges) {

    public record Range(long start, long end) {

        public Range {

            if (start < 0)
                throw InvalidInputException.with("Range.start must be greater than or equal to 0");

            if (end < start)
                throw InvalidInputException.with("Range.end must be greater than or equal to Range.start");

        }

    }

}
