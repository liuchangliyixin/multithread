package org.yixiu.multithread.streamapi.test;

import org.yixiu.multithread.streamapi.model.Trader;
import org.yixiu.multithread.streamapi.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionIterTest {
    public static void main(String[] args) {
        //prepare data
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //action
        //(1) 找出2011年发生的所有交易，并按交易额排序（从低到高）。
        List<Transaction> allTrans = transactions.stream()
                .filter( x-> x.getYear() == 2011)
                //.sorted((v1,v2) -> v1.getValue()-v2.getValue())
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(allTrans);
        //(2) 交易员都在哪些不同的城市工作过？
        Set<String> areas = transactions.stream()
                .map(x -> x.getTrader().getCity())
                //.distinct().collect(Collectors.toList());
                .collect(Collectors.toSet());
        System.out.println(areas);
        //(3) 查找所有来自于剑桥的交易员，并按姓名排序。
        List<Trader> tradersFromCambridge = transactions.stream()
                //.map(x -> x.getTrader())
                .map(Transaction::getTrader)
                .filter(x -> x.getCity().equals("Cambridge"))
                .distinct()
                //.sorted((v1,v2) -> v1.getName().compareTo(v2.getName()))
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());
        System.out.println(tradersFromCambridge);
        //(4) 返回所有交易员的姓名字符串，按字母顺序排序。
        String tradersName = transactions.stream()
                .map(x -> x.getTrader().getName())
                .distinct()
                //.sorted((v1,v2) -> v1.compareTo(v2))
                .sorted()
                //.collect(Collectors.toList());
                //.reduce("",(s1,s2) -> s1+" "+s2);
                .collect(Collectors.joining(" "));
        System.out.println(tradersName);
        //(5) 有没有交易员是在米兰工作的？
        /*Optional<Transaction> filterMarioTransaction = transactions.stream()
                .filter(x -> x.getTrader().getCity().equals("Milan")).findAny();
        boolean isTraderInMario = filterMarioTransaction.isPresent();*/
        Boolean isTraderInMario = transactions.stream()
                .anyMatch( x -> x.getTrader().getCity().equals("Milan"));
        System.out.println("Is any trader in Milan? " + isTraderInMario);
        //(6) 打印生活在剑桥的交易员的所有交易额。
        /*List<Integer> traderValuesFromCambridge = */transactions.stream()
                .filter(x -> x.getTrader().getCity().equals("Cambridge"))
                .map(x -> x.getValue())
                /*.collect(Collectors.toList());
        System.out.println(traderValuesFromCambridge);*/
                .forEach(System.out::println);
        //(7) 所有交易中，最高的交易额是多少？
        Optional<Integer> maxValue = transactions.stream()
                //.map(x ->x.getValue())
                .map(Transaction::getValue)
                .reduce(Integer::max);
        System.out.println("最高交易额: " + maxValue.get());
        //(8) 找到交易额最小的交易。
        Optional<Transaction> minTransaction = transactions.stream()
                /*.sorted( (v1,v2) -> v1.getValue() - v2.getValue())
                .findFirst();*/
                //.reduce( (v1,v2) -> v1.getValue() < v2.getValue() ? v1 : v2);
                .min(Comparator.comparing(Transaction::getValue));
        System.out.println(minTransaction);
    }
}
