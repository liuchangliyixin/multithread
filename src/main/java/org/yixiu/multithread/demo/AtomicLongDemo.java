package org.yixiu.multithread.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongDemo {

    public static AtomicLong counter = new AtomicLong();

    public static int[] arrayOne = new int[]{1,3,6,0,9,0,8};

    public static int[] arrayTwo = new int[]{2,0,6,0,7,0,12};

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(() -> {
            int size = arrayOne.length;
            for(int i=0;i<size;i++){
                if(arrayOne[i] == 0){
                    counter.getAndIncrement();
                }
            }
            latch.countDown();
        }).start();

        new Thread(() -> {
            int size = arrayTwo.length;
            for(int i=0;i<size;i++){
                if(arrayTwo[i] == 0){
                    counter.getAndIncrement();
                }
            }
            latch.countDown();
        }).start();

        latch.await();

        System.out.println("Number Zero Num : " + counter.get());
    }
}
