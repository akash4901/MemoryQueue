package org.aks.message;

public interface Message<T> {
    long getId();

    int getTtl();

    T getMessage();

    boolean isExpired();

}
