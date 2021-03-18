package com.payee.test.utils;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoUtils {
    /**
     * Remove provided keys from the existing DTO
     *
     * @param field {string keys to remove from the main model}
     * @return updated DTO model
     */
    public static Object removeFromDto(Object dto, String field) {
        ObjectMapper objectMapper = new ObjectMapper();
        var newMap = objectMapper.convertValue(dto, Map.class);
        newMap.keySet().remove(field);
        return objectMapper.convertValue(newMap, Object.class);
    }
}
