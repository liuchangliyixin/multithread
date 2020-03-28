package org.yixiu.multithread.thread;

import java.util.concurrent.*;

public class MultiThreadFindMax {

    public static int max(int[] data) throws InterruptedException, ExecutionException {
        if(data.length == 1){
            return data[0];
        }else if(data.length == 0){
            throw new IllegalArgumentException();
        }

        FindMaxTask task1 = new FindMaxTask(data,0,data.length/2);
        FindMaxTask task2 = new FindMaxTask(data,data.length/2,data.length);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future1 = executor.submit(task1);
        Future<Integer> future2 = executor.submit(task2);

        return Math.max(future1.get(),future2.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] data = new int[100000];
        for(int i =1;i<100000;i++){
            data[i] = i;
        }
        System.out.println("data array max value : " + max(data));
    }
}

class FindMaxTask implements Callable<Integer>{

    private int[] data;
    private int start;
    private int end;

    public FindMaxTask(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        int max = Integer.MIN_VALUE;
        for(int i = start;i < end;i++){
            if(data[i] > max)
                max = data[i];
        }
        return max;
    }
}