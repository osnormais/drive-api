package org.osnormais.drive.api.infrastructure.commons;

import static java.util.Objects.nonNull;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class RestLocationBuilder {

    private RestLocationBuilder() {
    }

    public static URI buildLocation(final String path, final Object... uriVariableValues) {
        final var builder = ServletUriComponentsBuilder.fromCurrentRequest();

        if (nonNull(path))
            builder.path(path);

        if (nonNull(uriVariableValues) && uriVariableValues.length > 0)
            return builder.buildAndExpand(uriVariableValues).toUri();

        return builder.build().toUri();
    }

}
