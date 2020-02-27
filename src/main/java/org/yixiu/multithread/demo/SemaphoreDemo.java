package org.yixiu.multithread.demo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        for(int i =1;i<11;i++){
            new Car(i,semaphore).start();
        }
    }

    static class Car extends Thread{
        private int num;
        private Semaphore semaphore;

        public Car(int num,Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run(){
            try {
                semaphore.acquire();
                System.out.println("the NO."+num+ " car take a site!");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("the NO."+num+" car leave away!");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
