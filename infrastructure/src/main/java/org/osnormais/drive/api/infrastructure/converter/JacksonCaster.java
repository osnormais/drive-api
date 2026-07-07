package org.osnormais.drive.api.infrastructure.converter;

import org.osnormais.drive.api.infrastructure.configuration.mapper.Mapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JacksonCaster implements Caster {

    private static final ObjectMapper mapper = Mapper.mapper();

    @Override
    public <T> T cast(Object value, Class<T> targetType) {
        return mapper.convertValue(value, targetType);
    }

}
