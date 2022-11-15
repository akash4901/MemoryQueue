package org.aks.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aks.message.Message;

public class JsonFormatValidator implements MessageValidator<Message<String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean validate(Message<String> stringMessage) {
        try {
            String message = stringMessage.getMessage();
            objectMapper.readTree(message);
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
