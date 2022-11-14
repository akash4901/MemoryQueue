package org.aks.consumer;

import org.aks.queue.Queue;

public abstract class AbstractConsumer<T> implements Consumer{
    final Queue<T> queue;
    final String consumerName;
    final int retryMaxCount =3;
    private MessageFilter<T> messageFilter;

    public AbstractConsumer(Queue queue,String consumerName) {
        this.queue = queue;
        this.consumerName =consumerName;
    }
    public AbstractConsumer(Queue queue,String consumerName,MessageFilter<T> messageFilter) {
        this.queue = queue;
        this.consumerName =consumerName;
        this.messageFilter =messageFilter;
    }

    @Override
    public void process() {
         if(messageFilter!= null){
            T message = queue.poll(messageFilter);
//            while(messageFilter.validate(message)){
//
//            }
        }
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
              //TODO handle failure;
            }
        }
    }
    protected void processNext(){
        T message = queue.poll();
        handleMessage(message,0);
    }

    public abstract void processMessage(T message);
}
