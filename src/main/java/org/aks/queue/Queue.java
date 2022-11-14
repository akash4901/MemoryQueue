package org.aks.queue;


import org.aks.consumer.MessageFilter;

public interface Queue<E>{

    boolean offer(E e);
    E poll();
    E poll(MessageFilter messageFilter);
    int size();
}
