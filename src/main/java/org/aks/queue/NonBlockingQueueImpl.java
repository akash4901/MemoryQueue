package org.aks.queue;

import org.aks.consumer.MessageFilter;
import org.aks.message.Message;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @param <E>
 */
public class NonBlockingQueueImpl<E> implements Queue<E> {

    private final AtomicReference<CircularQueuePtr> queuePtrRef = new AtomicReference<>(new CircularQueuePtr());
    private final E[] queue;

    public NonBlockingQueueImpl(int queueSize) {
        queue = (E[]) new Object[queueSize];
    }

    @Override
    public boolean offer(E e) {
        boolean result = false;
        boolean casResult = false;
        while (!casResult) {
            CircularQueuePtr queuePtrRef = this.queuePtrRef.get();
            if (!isFull(queuePtrRef)) {
                int newTail = (queuePtrRef.getTail() + 1) % queue.length;
                int newHead = queuePtrRef.getHead() == -1 ? 0 : queuePtrRef.getHead();
                CircularQueuePtr updatedObj = new CircularQueuePtr(newHead, newTail);
                casResult = this.queuePtrRef.compareAndSet(queuePtrRef, updatedObj);
                if (casResult) {
                    queue[newTail] = e;
                    result = true;
                    System.out.println(Thread.currentThread().getName() + " :: New message " + ((Message<String>) e).getMessage() + " added to the queue, queueSize=" + size(updatedObj));
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " :: Queue Full... Could not add message " + ((Message<String>) e).getMessage());
                result = false;
                casResult = true;
            }
        }
        return result;
    }

    @Override
    public E poll() {
        E res = null;
        boolean casResult = false;
        while (!casResult) {
            CircularQueuePtr queuePtrRef = this.queuePtrRef.get();
            if (!isEmpty(queuePtrRef)) {
                //if head and tail are same it means we have only one element in queue
                if (queuePtrRef.head == queuePtrRef.tail) {
                    res = queue[queuePtrRef.head];
                    //update head to -1 and tail to 0 as queue is now empty
                    casResult = this.queuePtrRef.compareAndSet(queuePtrRef, new CircularQueuePtr(-1, 0));
                    if (casResult) {
                        queue[queuePtrRef.head] = null;
                        System.out.println("Message removed from Queue, message= " + ((Message<String>) res).getMessage() + " :: queueSize=" + size(this.queuePtrRef.get()));
                    }
                } else {
                    res = queue[queuePtrRef.head];
                    int newHead = (queuePtrRef.head + 1) % queue.length;
                    casResult = this.queuePtrRef.compareAndSet(queuePtrRef, new CircularQueuePtr(newHead, queuePtrRef.tail));
                    if (casResult) {
                        queue[newHead] = null;
                        System.out.println("#Message removed from Queue, message= " + ((Message<String>) res).getMessage() + " :: queueSize=" + size(this.queuePtrRef.get()));
                    }
                }
            } else {
                casResult = true;
            }
        }
        return res;
    }

    @Override
    public E poll(MessageFilter messageFilter) {
        E res = null;
        boolean casResult = false;
        while (!casResult) {
            CircularQueuePtr queuePtrRef = this.queuePtrRef.get();
            if (!isEmpty(queuePtrRef)) {
                //if head and tail are same it means we have only one element in queue
                res = queue[queuePtrRef.head];
                if (!isMatch(messageFilter, res)) {
                    res = null;
                } else {
                    if (((Message<?>) res).isExpired()) {
                        System.out.println(Thread.currentThread().getName() + " :: Message Expired, message=" + ((Message<?>) res).getMessage());
                        res = null;
                    }

                    if (queuePtrRef.head == queuePtrRef.tail) {
                        //update head to -1 and tail to 0 as queue is now empty
                        CircularQueuePtr updated = new CircularQueuePtr(-1, -1);
                        casResult = this.queuePtrRef.compareAndSet(queuePtrRef, updated);
                        if (casResult) {
                            queue[queuePtrRef.head] = null;
                            System.out.println(Thread.currentThread().getName() + " :: Message removed from Queue, message= " + ((Message<String>) res).getMessage() + " :: queueSize=" + size(updated));
                        }
                    } else {
                        int newHead = (queuePtrRef.head + 1) % queue.length;
                        CircularQueuePtr updated = new CircularQueuePtr(newHead, queuePtrRef.tail);
                        casResult = this.queuePtrRef.compareAndSet(queuePtrRef, updated);
                        if (casResult) {
                            queue[queuePtrRef.head] = null;
                            System.out.println(Thread.currentThread().getName() + " :: Message removed from Queue, message= " + ((Message<String>) res).getMessage() + " :: queueSize=" + size(updated));
                        }
                    }
                }
            } else {
                casResult = true;
            }
        }

        return res;
    }

    private boolean isMatch(MessageFilter messageFilter, E res) {
        boolean isMatch = false;
        if (res != null && messageFilter.isMatch(res)) {
            isMatch = true;
        }
        return isMatch;
    }

    private boolean isEmpty(CircularQueuePtr queuePtrRef) {
        return queuePtrRef.getHead() == -1;
    }

    private boolean isFull(CircularQueuePtr queuePtrRef) {
        return (queuePtrRef.tail + 1) % queue.length == queuePtrRef.head;
    }

    @Override
    public int size() {
        CircularQueuePtr queuePtrRef = this.queuePtrRef.get();
        return (queuePtrRef.tail - queuePtrRef.head + 1) % queue.length;
    }

    public int size(CircularQueuePtr queuePtrRef) {
        if (queuePtrRef.tail == -1 && queuePtrRef.tail == queuePtrRef.head) {
            return 0;
        } else if (queuePtrRef.tail >= queuePtrRef.head) {
            return queuePtrRef.tail - queuePtrRef.head + 1;
        } else {
            return queue.length - (queuePtrRef.head - queuePtrRef.tail - 1);
        }
    }




    private static class CircularQueuePtr {
        private final int head;
        private final int tail;

        public CircularQueuePtr() {
            this.head = -1;
            this.tail = -1;
        }

        public CircularQueuePtr(int head, int tail) {
            this.head = head;
            this.tail = tail;
        }

        public int getHead() {
            return head;
        }

        public int getTail() {
            return tail;
        }
    }
}

