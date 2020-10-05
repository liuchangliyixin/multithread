package org.yixiu.multithread.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author yixiu
 * @title: WaitNotifyDemo
 * @projectName multithread
 * @description: TODO
 * @date 2020/10/513:07
 */
public class WaitNotifyDemo {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        new Thread(new Producer(queue,5)).start();
        new Thread(new Consumer(queue)).start();
    }

    static class Producer implements Runnable{
        private Queue<String> queue;
        private int size;

        public Producer(Queue<String> queue, int size) {
            this.queue = queue;
            this.size = size;
        }

        @Override
        public void run() {
            int i = 0;
            while (true){
                i++;
                synchronized (queue){
                    while(queue.size() == size){
                        System.out.println("生产队列已满...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("生产 product-" + i);
                    queue.add("product-" + i);
                    queue.notifyAll();
                }
            }
        }
    }

    static class Consumer implements Runnable{
        private Queue<String> queue;

        public Consumer(Queue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true){
                synchronized (queue){
                    while(queue.isEmpty()){
                        System.out.println("消费队列已空...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("消费 "+queue.remove());
                    queue.notifyAll();
                }
            }
        }
    }
}
