package com.ash.jsonschema.service;

import com.ash.jsonschema.impl.JsonSchemaObjectMapperFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by agupt12 on 2/2/17 for jsonschema-maven-plugin
 *
 *
 */
public class CustomMapper implements JsonSchemaObjectMapperFactory {

    @Override public ObjectMapper createCustomMapper() {
            ObjectMapper serializeMapper = new ObjectMapper();
            serializeMapper = new ObjectMapper();
            serializeMapper.setVisibilityChecker(serializeMapper.getSerializationConfig().getDefaultVisibilityChecker()
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                    .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
            serializeMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            serializeMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            return serializeMapper;
    }
}
