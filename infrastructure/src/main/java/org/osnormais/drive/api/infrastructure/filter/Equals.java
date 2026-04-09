package org.osnormais.drive.api.infrastructure.filter;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.infrastructure.converter.Caster;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class Equals extends SpecificationFilter {

    public Equals(final Caster caster) {
        super(caster);
    }

    @Override
    public Filter.Type filterType() {
        return Filter.Type.EQUALS;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {
        return (root, query, criteriaBuilder) -> {
            final var field = root.get(filter.field().value());
            return criteriaBuilder.equal(field, cast(filter.value(), field.getJavaType()));
        };
    }

}
