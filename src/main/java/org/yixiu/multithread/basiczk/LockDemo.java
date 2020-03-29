package org.yixiu.multithread.basiczk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class LockDemo {
    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConstant.CONNECT_STR).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();

        /**
         * 	InterProcessMutex：分布式可重入排它锁
         * 	InterProcessSemaphoreMutex：分布式排它锁
         * 	InterProcessReadWriteLock：分布式读写锁
         */
        InterProcessMutex lock = new InterProcessMutex(curatorFramework,"/lock");
        for(int i=0;i<10;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" try acquire lock!");
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName()+" acquire lock!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        lock.release();
                        System.out.println(Thread.currentThread().getName()+" release lock!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },"Thread-"+i).start();
        }
    }
}
