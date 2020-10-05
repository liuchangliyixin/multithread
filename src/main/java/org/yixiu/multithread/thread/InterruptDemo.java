package org.yixiu.multithread.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author yixiu
 * @title: InterruptDemo
 * @projectName multithread
 * @description: TODO
 * @date 2020/10/512:34
 */
public class InterruptDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    //线程被中断 中断逻辑处理
                    System.out.println("before Interrupted:" + Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupted();//对中断标识复位  不影响后面的逻辑
                    System.out.println("after Interrupted:" + Thread.currentThread().isInterrupted());
                }
            }
        });

        thread.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();//中断thread线程
    }
}
