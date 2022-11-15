package org.aks.consumer;

import java.util.Iterator;
import java.util.List;

public interface DeadLetterQueue<E> {
    void add(E e);
    Iterator<E> getMessages();
}
