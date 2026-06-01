package org.osnormais.drive.api.domain.exception;

import java.util.Arrays;
import java.util.List;

import org.osnormais.drive.api.domain.pagination.Filter;

public class InvalidFilterException extends SilentDomainException {

    private InvalidFilterException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static InvalidFilterException filter(final String passedFilter, final String example) {
        return new InvalidFilterException(
                "The filter is invalid.",
                List.of(
                        Error.with("Invalid filter: " + passedFilter),
                        Error.with("Example of a valid filter: " + example)));
    }

    public static InvalidFilterException field(
            final String fieldPassed,
            final List<Filter.Field> possibleFields) {
        return new InvalidFilterException(
                "The filter field is invalid.",
                List.of(Error.with(
                        "Invalid filter field: "
                                + fieldPassed
                                + ". Possible fields are: " +
                                Arrays.toString(possibleFields.toArray()))));
    }

    public static InvalidFilterException operator(
            final String filterPassed,
            final List<Filter.Operator> possibleOperators) {
        return new InvalidFilterException(
                "The filter operator is invalid.",
                List.of(Error.with(
                        "Invalid filter operator: "
                                + filterPassed
                                + ". Possible operators are: "
                                + Arrays.toString(possibleOperators.toArray()))));
    }

    public static InvalidFilterException type(
            final String typePassed,
            final Filter.Field field) {
        return new InvalidFilterException(
                "The filter type is invalid.",
                List.of(Error.with(
                        "Invalid filter type: "
                                + typePassed
                                + " for field "
                                + field.value()
                                + ". Possible types are: "
                                + Arrays.toString(field.supportedFilterTypes().toArray()))));
    }

}
