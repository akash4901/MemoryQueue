package org.aks.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aks.json.MessagePojo;
import org.aks.message.Message;

public class HttpMessageFilter implements MessageFilter<Message<String>> {

    private final int requiredHttpCode;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpMessageFilter(int desiredHttpCode) {
        this.requiredHttpCode = desiredHttpCode;
    }

    @Override
    public boolean isMatch(Message<String> message) {
        String json = message.getMessage();

        MessagePojo usrMsg = null;
        try {
            usrMsg = mapper.readValue(json, MessagePojo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (usrMsg.getHttpCode() == this.requiredHttpCode) {
                return true;
        }else {
            return false;
        }


    }

    @Override
    public String toString() {
        return "HttpCodeSelector{" +
                "desiredHttpCode=" + requiredHttpCode +
                '}';
    }
}
