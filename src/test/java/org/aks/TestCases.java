package org.aks;

import org.aks.consumer.*;
import org.aks.message.Message;
import org.aks.producer.Producer;
import org.aks.producer.ProducerImpl;
import org.aks.queue.NonBlockingQueueImpl;
import org.aks.queue.Queue;
import org.aks.validation.JsonFormatValidator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCases {
    @Test //produce and cnsumer
    public void test1(){
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(3,new JsonFormatValidator());
        Producer<String> producer = new ProducerImpl<>(messageQueue,3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\"}";
        String msg2 = "{\"messageId\": \"def\"}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = TestUtil.producerThread(producer,messageQueue,messages,6000);
        String msg3 = "{\"messageId\": \"xyz\"}";
        messages1.add(msg3);
        Thread ProducerThread2 = TestUtil.producerThread(producer,messageQueue,messages1,6000);

        Consumer consumer1 = new ConsumerImpl(messageQueue, "1");
        Thread consumerTh1 = TestUtil.consumerThread(consumer1);
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2");
        Thread consumerTh2 = TestUtil.consumerThread(consumer2);

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
    /**
     * Message expiration test. Queue will skip expired message and poll out non expired one to consumer
     */
    @Test
    public void test2() {
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(4,new JsonFormatValidator());
        Producer<String> producer = new ProducerImpl<>(messageQueue,3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\"}";
        String msg2 = "{\"messageId\": \"def\"}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = TestUtil.producerThread(producer,messageQueue,messages,6);
        String msg3 = "{\"messageId\": \"xyz\"}";
        String msg4 = "{\"messageId\": \"fgh\"}";
        messages1.add(msg3);messages1.add(msg4);
        Thread ProducerThread2 = TestUtil.producerThread(producer,messageQueue,messages1,6000);

        Consumer consumer1 = new ConsumerImpl(messageQueue, "1");
        Thread consumerTh1 = TestUtil.consumerThread(consumer1);
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2");
        Thread consumerTh2 = TestUtil.consumerThread(consumer2);

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
    /*
    JSoN valiodatoro
     */
    @Test
    public void test3() {
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(3,new JsonFormatValidator());
        Producer<String> producer = new ProducerImpl<>(messageQueue,3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\"}";
        String msg2 = "{\"messageId\": \"def\"}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = TestUtil.producerThread(producer,messageQueue,messages,6000);
        String msg3 = "{\"messageId\": \"xyz\"";
        messages1.add(msg3);
        Thread ProducerThread2 = TestUtil.producerThread(producer,messageQueue,messages1,6000);

        Consumer consumer1 = new ConsumerImpl(messageQueue, "1");
        Thread consumerTh1 = TestUtil.consumerThread(consumer1);
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2");
        Thread consumerTh2 = TestUtil.consumerThread(consumer2);

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
    /*
    filter test
     */
    @Test
    public void test4() {
        Queue<Message<String>> messageQueue = new NonBlockingQueueImpl<>(4,new JsonFormatValidator());
        Producer<String> producer = new ProducerImpl<>(messageQueue,3);
        List<String> messages = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();
        String msg1 = "{\"messageId\": \"abc\", \"httpCode\": 200}";
        String msg2 = "{\"messageId\": \"def\", \"httpCode\": 200}";
        messages.add(msg1);messages.add(msg2);
        Thread ProducerThread1 = TestUtil.producerThread(producer,messageQueue,messages,6000);
        String msg3 = "{\"messageId\": \"xyz\", \"httpCode\": 400}";
        String msg4 = "{\"messageId\": \"hgj\", \"httpCode\": 200}";
        messages1.add(msg3);messages1.add(msg4);
        Thread ProducerThread2 = TestUtil.producerThread(producer,messageQueue,messages1,6000);

        MessageFilter<Message<String>> filter1 = new HttpMessageFilter(200);
        DeadLetterQueue<Message<String>> deadLetterQueue1 = new DeadLetterQueueImpl<>();
        Consumer consumer1 = new ConsumerImpl(messageQueue, "1",filter1,deadLetterQueue1);
        Thread consumerTh1 = TestUtil.consumerThread(consumer1);
        MessageFilter<Message<String>> filter2 = new HttpMessageFilter(400);
        DeadLetterQueue<Message<String>> deadLetterQueue2 = new DeadLetterQueueImpl<>();
        Consumer consumer2 = new ConsumerImpl(messageQueue, "2",filter2,deadLetterQueue2);
        Thread consumerTh2 = TestUtil.consumerThread(consumer2);

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

}