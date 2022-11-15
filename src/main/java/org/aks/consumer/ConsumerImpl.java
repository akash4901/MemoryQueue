package org.aks.consumer;

import org.aks.message.Message;
import org.aks.queue.Queue;

public class ConsumerImpl extends AbstractConsumer<Message<String>> {
   public ConsumerImpl(Queue queue, String consumerName) {
        super(queue, consumerName,new DummyMessageFilterImpl<>(),new DeadLetterQueueImpl<>());
   }
    public ConsumerImpl(Queue queue, String consumerName, MessageFilter<Message<String>> filter,DeadLetterQueue<Message<String>> deadLetterQueue) {
        super(queue, consumerName,filter,deadLetterQueue);
    }

    @Override
    public void processMessage(Message<String> message) {
        if (message != null) {
            String msg  = message.getMessage();
            System.out.println(Thread.currentThread().getName()+" : Consumer " + this.consumerName + " consumed message : " + msg);
        }
    }
}
