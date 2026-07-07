package org.osnormais.drive.api.infrastructure.filter;

import java.util.List;
import java.util.Objects;

import org.osnormais.drive.api.domain.pagination.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FilterService {

    private final List<SpecificationFilter> filters;

    public FilterService(final List<SpecificationFilter> filters) {
        this.filters = List.copyOf(Objects.requireNonNull(filters));
    }

    public <T> Specification<T> build(
            final Class<T> entityClass,
            final Filter.Operator groupOperator,
            final List<Filter.Group> filterGroups) {

        return switch (groupOperator) {
            case AND -> andSpecifications(filterGroups
                    .stream()
                    .map(group -> buildGroupSpecification(entityClass, group))
                    .toList());
            case OR -> orSpecifications(filterGroups
                    .stream()
                    .map(group -> buildGroupSpecification(entityClass, group))
                    .toList());
        };

    }

    private <T> Specification<T> buildGroupSpecification(
            final Class<T> entityClass,
            final Filter.Group group) {

        final var elementsIterator = group.elements().iterator();

        if (!elementsIterator.hasNext())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        final var firstElement = elementsIterator.next();
        Specification<T> groupSpecification = buildSpecification(entityClass, firstElement.filter());

        while (elementsIterator.hasNext()) {
            groupSpecification = buildElementSpecification(entityClass, groupSpecification, elementsIterator.next());
        }

        return groupSpecification;

    }

    private <T> Specification<T> buildElementSpecification(
            final Class<T> entityClass,
            final Specification<T> groupSpecification,
            final Filter.Group.Element element) {

        return switch (element.operator()) {
            case AND -> groupSpecification.and(buildSpecification(entityClass, element.filter()));
            case OR -> groupSpecification.or(buildSpecification(entityClass, element.filter()));
        };

    }

    private <T> Specification<T> buildSpecification(
            final Class<T> entityClass,
            final Filter filter) {
        final var specification = filters.stream()
                .filter(f -> f.filterType().equals(filter.type()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Filter not found"));

        return specification.buildSpecification(filter);
    }

    private static <T> Specification<T> orSpecifications(final List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::or)
                .orElse(emptySpec());
    }

    private static <T> Specification<T> andSpecifications(final List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(emptySpec());
    }

    private static <T> Specification<T> emptySpec() {
        return (root, query, cb) -> cb.conjunction();
    }

}
