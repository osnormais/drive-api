package org.osnormais.drive.api.infrastructure.filter;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.infrastructure.converter.Caster;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class Like extends SpecificationFilter {

    public Like(final Caster caster) {
        super(caster);
    }

    @Override
    public Filter.Type filterType() {
        return Filter.Type.LIKE;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(
                        criteriaBuilder.upper(criteriaBuilder.toString(root.get(filter.field().value()))),
                        "%" + filter.value().toUpperCase() + "%");
    }

    private void validateFilter(final Filter filter) {
        if (filter.value() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!Filter.Type.LIKE.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be LIKE");
    }

}
