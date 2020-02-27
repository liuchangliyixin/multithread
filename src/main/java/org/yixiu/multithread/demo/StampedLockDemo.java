package org.yixiu.multithread.demo;

import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {
    public static void main(String[] args) {

    }

    static class Point{
        private int x;
        private int y;
        final StampedLock stampedLock = new StampedLock();

        double distanceFromOrigin(){
            long stamp = stampedLock.tryOptimisticRead();
            int curx = x,cury = y;
            if(!stampedLock.validate(stamp)){
                stamp = stampedLock.readLock();
                try {
                    curx = x;
                    cury = y;
                }finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(curx*curx + cury*cury);
        }
    }
}
