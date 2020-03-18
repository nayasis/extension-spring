package io.nayasis.spring.extension.config.mapper;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

public class ObjectMapperBuilder {

    public ObjectMapper fieldMapper() {
        return fieldMapper( false );
    }

    public ObjectMapper fieldMapper( boolean removeXss ) {
        ObjectMapper objectMapper = buildFieldMapper();
        if( removeXss ) {
            objectMapper.getFactory().setCharacterEscapes( new HtmlCharacterEscapes() );
        }
        return objectMapper;
    }

    private ObjectMapper buildFieldMapper() {
        return Jackson2ObjectMapperBuilder.json()
            .featuresToDisable( SerializationFeature.FAIL_ON_EMPTY_BEANS )
            .featuresToDisable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS )
            .featuresToEnable( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY )
            .featuresToEnable( JsonParser.Feature.ALLOW_SINGLE_QUOTES )
            .visibility( PropertyAccessor.ALL, NONE )
            .visibility( PropertyAccessor.FIELD, ANY )
            .modules( new JavaTimeModule() )
            .build();
    }

}
