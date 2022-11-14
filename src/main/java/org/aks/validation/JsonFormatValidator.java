package org.aks.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFormatValidator implements MessageValidator<String> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean validate(String s) {
        try {
            objectMapper.readTree(s);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

    @Override
    public String getDetails() {
        return (" only JSON format is allowed");
    }
}
