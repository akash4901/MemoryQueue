package org.aks.consumer;

public class DummyMessageFilterImpl<E> implements MessageFilter<E> {
    @Override
    public boolean isMatch(E e) {
        return true;
    }
}
