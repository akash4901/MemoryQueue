package org.aks.consumer;

public interface MessageFilter<E> {
    boolean isMatch(E e);
}
