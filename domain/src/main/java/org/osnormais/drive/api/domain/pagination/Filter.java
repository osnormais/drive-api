package org.osnormais.drive.api.domain.pagination;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.Notification;

public record Filter(Field field, String value, String valueToCompare, Type type) {

    public Filter {

        final Notification notification = Notification.create();
        if (isNull(field))
            notification.append(ValidationError.with("Filter.field cannot be null"));

        if (nonNull(field) && field.value().isBlank())
            notification.append(ValidationError.with("Filter.field cannot be blank"));

        if (isNull(value))
            notification.append(ValidationError.with("Filter.value cannot be null"));

        if (isNull(type))
            notification.append(
                    ValidationError.with(
                            "Filter.type cannot be null, Valid values are: " + Arrays.toString(Type.values())));

        if (notification.hasErrors())
            throw ValidationException.with("Filter contains invalid parameters", notification);

    }

    public String valueToCompare() {
        return isNull(valueToCompare) ? value : valueToCompare;
    }

    public enum Type {

        EQUALS,
        LIKE,
        BETWEEN;

        public static Optional<Type> of(final String type) {
            if (isNull(type))
                return Optional.empty();

            return Arrays.stream(Type.values())
                    .filter(it -> it.name().equalsIgnoreCase(type))
                    .findFirst();
        }

    }

    public enum Operator {
        AND, OR;

        public static Optional<Operator> of(final String type) {
            if (isNull(type))
                return Optional.empty();

            return Arrays.stream(Operator.values())
                    .filter(it -> it.name().equalsIgnoreCase(type))
                    .findFirst();
        }
    }

    public record Group(List<Group.Element> elements) {

        public record Element(Filter.Operator operator, Filter filter) {
        }

    }

    public interface Field {

        String name();

        String value();

        Set<Filter.Type> supportedFilterTypes();

        default Boolean matches(String name) {
            return this.name().equalsIgnoreCase(name);
        }

        default Boolean supports(Filter.Type type) {
            return supportedFilterTypes().contains(type);
        }

    }

}
