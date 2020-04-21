package org.yixiu.multithread.streamapi.test;

import java.util.stream.Stream;

public class IterateStreamTest {
    public static void main(String[] args) {
        Stream.iterate(new int[]{0,1},x -> new int[]{x[1],x[0]+x[1]})
                .limit(10)
                .forEach(x -> System.out.println("(" + x[0] + "," + x[1] + ")"));
    }
}
