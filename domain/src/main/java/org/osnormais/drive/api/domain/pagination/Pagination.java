package org.osnormais.drive.api.domain.pagination;

public record Pagination(
        int page,
        int perPage,
        Order order) {

    public static Pagination of(final int page, final int perPage, final Order order) {
        return new Pagination(page, perPage, order);
    }

    public record Order(Filter.Field field, Direction direction) {

        public static Order of(final Filter.Field field, final Direction direction) {
            return new Order(field, direction);
        }

        public enum Direction {
            ASC, DESC
        }
    }

}
