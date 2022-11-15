package org.aks;

import org.aks.consumer.Consumer;
import org.aks.message.Message;
import org.aks.producer.Producer;
import org.aks.queue.Queue;

import java.util.List;

public class TestUtil {
    public static Thread producerThread(Producer<String> producer, Queue<Message<String>> queue, List<String> messages, int ttl) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (String msg : messages) {
                    producer.send(msg, ttl);
                }
            }
        });
    }

    public static Thread consumerThread(Consumer consumer) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    consumer.process();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
