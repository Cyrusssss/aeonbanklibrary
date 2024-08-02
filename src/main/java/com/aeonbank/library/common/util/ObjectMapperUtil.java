package com.aeonbank.library.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapperIncludeNullFailTrue;
    private static final ObjectMapper objectMapperIncludeNullFailFalse;
    private static final ObjectMapper objectMapperExcludeNullFailTrue;
    private static final ObjectMapper objectMapperExcludeNullFailFalse;

    static {
        objectMapperIncludeNullFailTrue = createObjectMapper(JsonInclude.Include.ALWAYS, true);
        objectMapperIncludeNullFailFalse = createObjectMapper(JsonInclude.Include.ALWAYS, false);
        objectMapperExcludeNullFailTrue = createObjectMapper(JsonInclude.Include.NON_NULL, true);
        objectMapperExcludeNullFailFalse = createObjectMapper(JsonInclude.Include.NON_NULL, false);
    }

    private static ObjectMapper createObjectMapper(JsonInclude.Include include, boolean failOnUnknownProperties) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(include);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper(){
        return objectMapperIncludeNullFailTrue;
    }

    public static ObjectMapper getObjectMapper(boolean excludeNull, boolean failUnknown){
        if (excludeNull) {
            if (failUnknown) {
                return objectMapperExcludeNullFailTrue;
            } else {
                return objectMapperExcludeNullFailFalse;
            }
        } else {
            if (failUnknown) {
                return objectMapperIncludeNullFailTrue;
            } else {
                return objectMapperIncludeNullFailFalse;
            }
        }
    }

}
