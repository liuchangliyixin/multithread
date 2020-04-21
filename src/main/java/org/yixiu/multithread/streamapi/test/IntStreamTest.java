package org.yixiu.multithread.streamapi.test;

import org.apache.jute.CsvOutputArchive;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntStreamTest {
    public static void main(String[] args) {
        Stream<double[]> gouGuNum = IntStream.rangeClosed(1,100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a,100)
                        .mapToObj(b -> new double[]{a,b,Math.sqrt(a*a+b*b)})
                        .filter(s -> (s[2] % 1 == 0)));
        gouGuNum.map(s -> (int)s[0] + " ---" + (int)s[1] + " ---" + (int)s[2]).forEach(System.out::println);
    }
}
