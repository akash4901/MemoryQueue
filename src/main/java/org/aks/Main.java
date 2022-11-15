package org.aks;

import org.aks.consumer.Consumer;
import org.aks.consumer.ConsumerImpl;
import org.aks.consumer.HttpMessageFilter;
import org.aks.consumer.MessageFilter;
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

    }

}