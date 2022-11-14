package org.aks;

import org.aks.consumer.Consumer;
import org.aks.consumer.ConsumerImpl;
import org.aks.message.Message;
import org.aks.producer.Producer;
import org.aks.producer.ProducerImpl;
import org.aks.queue.NonBlockingQueueImpl;
import org.aks.queue.Queue;
import org.aks.validation.JsonFormatValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        //test1();
        test2();

    }
    //Test Case 1 produce and cnsumer
    private static void test1() {
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(3);
        Producer<String> producer = new ProducerImpl<>(messageQueue,new JsonFormatValidator(),3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\"}";
        String msg2 = "{\"messageId\": \"def\"}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = producerThread(producer,messageQueue,messages,6000);
        String msg3 = "{\"messageId\": \"xyz\"}";
        messages1.add(msg3);
        Thread ProducerThread2 = producerThread(producer,messageQueue,messages1,6000);

        Consumer consumer1 = new ConsumerImpl(messageQueue, "1");
        Thread consumerTh1 = consumerThread(consumer1);
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2");
        Thread consumerTh2 = consumerThread(consumer2);

        ProducerThread1.start();
        ProducerThread2.start();
        consumerTh1.start();
        consumerTh2.start();

        try {
            ProducerThread1.join();
            ProducerThread2.join();
            consumerTh1.join();
            consumerTh2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //Test case2 queue is full
    private static void test2() {
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(3);
        Producer<String> producer = new ProducerImpl<>(messageQueue,new JsonFormatValidator(),3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\"}";
        String msg2 = "{\"messageId\": \"def\"}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = producerThread(producer,messageQueue,messages,6000);
        String msg3 = "{\"messageId\": \"xyz\"}";
        String msg4 = "{\"messageId\": \"fgh\"}";
        messages1.add(msg3);messages1.add(msg4);
        Thread ProducerThread2 = producerThread(producer,messageQueue,messages1,6000);

        Consumer consumer1 = new ConsumerImpl(messageQueue, "1");
        Thread consumerTh1 = consumerThread(consumer1);
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2");
        Thread consumerTh2 = consumerThread(consumer2);

        ProducerThread1.start();
        ProducerThread2.start();
        consumerTh1.start();
        consumerTh2.start();

        try {
            ProducerThread1.join();
            ProducerThread2.join();
            consumerTh1.join();
            consumerTh2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static Thread producerThread(Producer<String> producer,Queue<Message<String>> queue, List<String> messages,int ttl) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (String msg : messages) {
                    producer.send(msg, ttl);
                }
            }
        });
    }
    private static Thread consumerThread(Consumer consumer) {
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