package org.osnormais.drive.api.domain.pagination;

import java.util.List;

public record SearchQuery(
        Pagination pagination,
        Filter.Operator filterMethod,
        List<Filter.Group> filters) {

    public static SearchQuery of(
            final Pagination pagination,
            final Filter.Operator filterMethod,
            final List<Filter.Group> filters) {
        return new SearchQuery(pagination, filterMethod, filters);
    }

}
