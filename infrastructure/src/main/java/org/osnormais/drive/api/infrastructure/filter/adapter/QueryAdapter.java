package org.osnormais.drive.api.infrastructure.filter.adapter;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.exception.InvalidFilterException;
import org.osnormais.drive.api.domain.pagination.Filter;
import org.osnormais.drive.api.domain.pagination.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public interface QueryAdapter {

    static final String FILTER_PATTERN = "(?i)(?:(AND|OR))?\\((.*?)\\)";
    static final Pattern FILTER_REGEX = Pattern.compile(FILTER_PATTERN);

    static PageRequest of(final Pagination pagination) {
        return PageRequest.of(pagination.page(), pagination.perPage(), of(pagination.order()));
    }

    static Sort of(final Pagination.Order order) {

        if (order == null)
            return Sort.unsorted();

        return Sort.by(of(order.direction()), order.field().value());
    }

    static Direction of(final Pagination.Order.Direction direction) {
        return Direction.fromString(direction.name());
    }

    static Filter.Group of(final String source, final List<Filter.Field> acceptableFields) {

        if (isNull(source) || source.isBlank())
            return new Filter.Group(new ArrayList<>());

        final Matcher matcher = FILTER_REGEX.matcher(source);

        final List<Filter.Group.Element> filterGroupElements = new ArrayList<Filter.Group.Element>();

        while (matcher.find()) {

            final String operatorGroup = Optional
                    .ofNullable(matcher.group(1))
                    .orElse("AND")
                    .toUpperCase();

            final String filterGroup = matcher.group(2);

            final Filter.Operator operator = Filter.Operator
                    .of(operatorGroup)
                    .orElseThrow(
                            () -> InvalidFilterException.operator(operatorGroup, List.of(Filter.Operator.values())));

            final Map<String, String> map = List.of(filterGroup.split(";"))
                    .stream()
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(s -> getSafeArrayElement(s, 0), s -> getSafeArrayElement(s, 1)));

            final var field = acceptableFields
                    .stream()
                    .filter(f -> f.matches(map.get("field")))
                    .findFirst()
                    .orElseThrow(() -> InvalidFilterException.field(map.get("field"), acceptableFields));

            final var type = Filter.Type
                    .of(map.get("type"))
                    .filter(field::supports)
                    .orElseThrow(() -> InvalidFilterException.type(map.get("type"), field));

            final Filter filter = new Filter(
                    field,
                    map.get("value"),
                    map.get("valueToCompare"),
                    type);

            filterGroupElements.add(new Filter.Group.Element(operator, filter));
        }

        return new Filter.Group(filterGroupElements);
    }

    private static String getSafeArrayElement(final String[] array, final int index) {
        return index >= 0 && index < array.length ? array[index] : "";
    }

}
