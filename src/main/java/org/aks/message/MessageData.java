package org.aks.message;

public class MessageData<T> implements Message<T> {
    private final T messageData;
    private final int ttl;
    private final long timeStamp;

    /**
     * @param messageData
     * @param ttl        0 means no expiry
     */
    public MessageData(T messageData, int ttl) {
        this.messageData = messageData;
        this.ttl = ttl;
        timeStamp = System.currentTimeMillis();
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public int getTtl() {
        return ttl;
    }

    @Override
    public T getMessage() {
        return messageData;
    }

    @Override
    public boolean isExpired() {
        long diff = System.currentTimeMillis() - timeStamp;
        return ttl == 0 ? false : diff > ttl;
    }
}
