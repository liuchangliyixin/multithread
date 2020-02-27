package org.yixiu.multithread.demo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch cdl = new CountDownLatch(3);
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "--- executing!");
            cdl.countDown();
            System.out.println(Thread.currentThread().getName() + "--- executed!");
        },"t1").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "--- executing!");
            cdl.countDown();
            System.out.println(Thread.currentThread().getName() + "--- executed!");
        },"t2").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "--- executing!");
            cdl.countDown();
            System.out.println(Thread.currentThread().getName() + "--- executed!");
        },"t3").start();

        cdl.await();
        System.out.println("All Thread completed!");
    }
}
