package org.yixiu.multithread.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    public static void main(String[] args) {

    }

    static class Cache<K,V>{
        final Map<K,V> cacheMap = new HashMap<>();
        final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        final Lock read = readWriteLock.readLock();
        final Lock write = readWriteLock.writeLock();

        public V get(K key){
            V val = null;
            read.lock();
            try {
                val = cacheMap.get(key);
            }finally {
                read.unlock();
            }
            if(val != null){
                return val;
            }

            write.lock();
            try {
                val = cacheMap.get(key);
                if(val == null){
                    //query val....
                    val = (V)("value");
                    cacheMap.put(key,val);
                }
            }finally {
                write.unlock();
            }
            return val;
        }
    }
}
