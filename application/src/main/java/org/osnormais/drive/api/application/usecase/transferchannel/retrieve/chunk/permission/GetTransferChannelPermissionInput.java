package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission;

import static java.util.Objects.isNull;

import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.application.exception.InvalidInputException;

public record GetTransferChannelPermissionInput(
        UUID actorId,
        UUID transferChannelId,
        Set<Range> ranges) {

    private static final int MAX_RANGES = 10;

    public GetTransferChannelPermissionInput {

        if (isNull(ranges) || ranges.isEmpty())
            throw InvalidInputException.with("At least one range must be provided");

        if (ranges.size() > MAX_RANGES)
            throw InvalidInputException.with("Number of ranges must be less than or equal to " + MAX_RANGES);

    }

    public record Range(long start, long end) {

        private static final long MAX_INTERVAL = 512L;

        public Range {

            if (start < 0)
                throw InvalidInputException.with("Range.start must be greater than or equal to 0");

            if (end < start)
                throw InvalidInputException.with("Range.end must be greater than or equal to Range.start");

            if (end - start + 1 > MAX_INTERVAL)
                throw InvalidInputException.with("Range interval must be less than or equal to " + MAX_INTERVAL);

        }

    }

}
