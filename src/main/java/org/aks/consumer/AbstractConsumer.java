package org.aks.consumer;

import org.aks.queue.Queue;

public abstract class AbstractConsumer<T> implements Consumer{
    final Queue<T> queue;
    final String consumerName;
    final int retryMaxCount =3;
    private MessageFilter<T> messageFilter;

    private final DeadLetterQueue<T> deadLetterQueue;

    public AbstractConsumer(Queue queue,String consumerName,DeadLetterQueue<T> deadLetterQueue) {
        this.queue = queue;
        this.consumerName =consumerName;
        this.deadLetterQueue = deadLetterQueue;
    }
    public AbstractConsumer(Queue queue,String consumerName,MessageFilter<T> messageFilter,DeadLetterQueue<T> deadLetterQueue) {
        this.queue = queue;
        this.consumerName =consumerName;
        this.messageFilter =messageFilter;
        this.deadLetterQueue = deadLetterQueue;
    }

    @Override
    public void process() {
        T message = queue.poll(messageFilter);
        handleMessage(message,0);
    }

    private void handleMessage(T message, int retryCount) {
        try {
            processMessage(message);
        } catch (Exception e) {
            if (retryCount < retryMaxCount) {
                handleMessage(message, ++retryCount);
            } else {
             deadLetterQueue.add(message);
            }
        }
    }
    protected void processNext(){
        T message = queue.poll();
        handleMessage(message,0);
    }

    public abstract void processMessage(T message);
}
