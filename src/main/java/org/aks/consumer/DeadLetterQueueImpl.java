package org.aks.consumer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeadLetterQueueImpl<T> implements DeadLetterQueue<T>{
    private final List<T> message = new ArrayList<>();

    @Override
    public void add(T t) {
        message.add(t);
    }

    @Override
    public Iterator<T> getMessages() {
        return message.iterator();
    }
}
