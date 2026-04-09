package org.osnormais.drive.api.infrastructure.filter;

import java.util.Objects;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.infrastructure.converter.Caster;
import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationFilter {

    private final Caster caster;

    protected SpecificationFilter(final Caster caster) {
        this.caster = Objects.requireNonNull(caster);
    }

    abstract Filter.Type filterType();

    abstract <T> Specification<T> buildSpecification(Filter filter);

    protected <T> T cast(final Object value, final Class<T> clazz) {
        return caster.cast(value, clazz);
    }

}
