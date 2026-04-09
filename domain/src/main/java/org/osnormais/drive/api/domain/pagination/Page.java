package org.osnormais.drive.api.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Page<T>(
        int currentPage,
        int perPage,
        int totalPages,
        long total,
        List<T> items) {

    public <R> Page<R> map(final Function<T, R> mapper) {
        final List<R> newList = this.items
                .stream()
                .map(mapper)
                .toList();

        return new Page<>(currentPage(), perPage(), totalPages(), total(), newList);
    }
}
