package org.yixiu.multithread.demo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Random在多线程条件下有需要竞争种子原子变量的更新操作从而降低性能的缺点
 * 由是引入了ThreadLocalRandom类，使用ThreadLocal的原理，在每一个线程本地保存一个种子变量，在调用current方法时初始化
 * 在多线程下计算新种子时，是根据本地的种子变量进行更新，从而避免了竞争
 * 需要在每个线程里面调用ThreadLocalRandom.current()，这样才能产生不同的种子，从而生成不同的随机数
 */
public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        new Thread(() -> {
            for(int i=0; i<10 ;i++){
                System.out.println("Thread One Random Serial "+ i +"： " + ThreadLocalRandom.current().nextInt(10));
            }
        },"ThreadOne").start();

        new Thread(() -> {
            for(int i=0; i<10 ;i++){
                System.out.println("Thread Two Random Serial "+ i +"： " + ThreadLocalRandom.current().nextInt(10));
            }
        },"ThreadTwo").start();

    }
}
