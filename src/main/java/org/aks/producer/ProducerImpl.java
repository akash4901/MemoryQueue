package org.aks.producer;

import org.aks.exception.CustomException;
import org.aks.validation.MessageValidator;
import org.aks.message.Message;
import org.aks.message.MessageData;
import org.aks.queue.Queue;

public class ProducerImpl<M> implements Producer<M> {

    private final Queue<Message<M>> queue;
    private int retryCount = 0;

    public ProducerImpl(Queue<Message<M>> queue,int retryCount) {
        this.queue = queue;
        this.retryCount = retryCount;
    }

    public ProducerImpl(Queue<Message<M>> queue) {
        this.queue = queue;
    }



    @Override
    public boolean send(M message, int ttl) {
       Message<M> msg = new MessageData<>(message, ttl);
        if(retryCount>0){
            return sendRetry(msg);
        }else {
            return queue.offer(msg);
        }

    }


    private boolean sendRetry( Message<M> msg ) {
        boolean sent = queue.offer(msg);
        if (!sent) {
            int ctr = 0;
            while (ctr < retryCount) {
               try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sent = queue.offer(msg);
                ctr++;
                if (sent) {
                    System.out.println(Thread.currentThread().getName()+" : Message retry successful,  retryAttempt="+ctr + " :: noOfRetries="+retryCount);
                    break;
                }
            }
        }
        return false;
    }


}
