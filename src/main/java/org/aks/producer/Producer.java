package org.aks.producer;

public interface Producer<M> {
    boolean send(M e,int ttl);
 }
