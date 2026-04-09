package org.osnormais.drive.api.infrastructure.converter;

@FunctionalInterface
public interface Caster {

    <T> T cast(Object value, Class<T> targetType);

}
