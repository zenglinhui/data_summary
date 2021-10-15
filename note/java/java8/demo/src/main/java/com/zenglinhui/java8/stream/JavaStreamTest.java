package com.zenglinhui.java8.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zenglh
 * @date 2021/10/13 14:56
 */
public class JavaStreamTest {

    public static void main(String[] args) {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Trader> traders = Arrays.asList(raoul, mario, alan, brian);

        List<Transaction> transactions = Arrays.asList(
          new Transaction(brian, 2011, 300),
          new Transaction(raoul, 2012, 1000),
          new Transaction(raoul, 2011, 400),
          new Transaction(mario, 2012, 710),
          new Transaction(mario, 2012, 700),
          new Transaction(alan, 2012, 950)
        );

        //1. 找出2011年发生的所有交易，并按交易额排序(从低到高)
        List<Transaction> sortTransaction = transactions.stream().
                filter(t -> t.getYear() == 2011).
                sorted(Comparator.comparing(Transaction::getValue)).
                collect(Collectors.toList());
        sortTransaction.forEach(System.out::println);

        System.out.println("------------------------------");
        //2. 交易员都在哪些不同的城市工作过
        Map<String, List<Trader>> groupTraders = traders.stream().collect(Collectors.groupingBy(Trader::getCity));
        groupTraders.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });

        // 如果只要城市，用下面的代码
        List<String> cities = transactions.stream().
                map(t -> t.getTrader().getCity()).
                distinct().
                collect(Collectors.toList());
        cities.forEach(System.out::println);

        System.out.println("------------------------------");
        //3. 查找所有来自于剑桥的交易员，并按姓名排序
        List<Trader> sortTrader = traders.stream().
                filter(t -> "Cambridge".equals(t.getCity())).
                sorted(Comparator.comparing(Trader::getName)).
                collect(Collectors.toList());
        sortTrader.forEach(System.out::println);

        System.out.println("------------------------------");
        //4. 返回所有交易员的姓名字符串，按字母顺序排序。
        /*List<String> traderNames = traders.stream().
                map(Trader::getName).
                sorted().
                collect(Collectors.toList());
        traderNames.forEach(System.out::println);*/
        String traderStr = traders.stream().
                map(Trader::getName).
                sorted().
                collect(Collectors.joining(","));
        System.out.println(traderStr);

        System.out.println("------------------------------");

        //5. 有没有交易员是在米兰工作的。
        Trader trader = traders.stream().
                filter(t -> "Milan".equals(t.getCity())).
                findAny().
                orElse(new Trader("none", "none"));
        System.out.println(trader);

        System.out.println("------------------------------");
        //6. 打印生活在剑桥的交易员的所有交易额
        transactions.stream().
                filter(t -> "Cambridge".equals(t.getTrader().getCity())).
                mapToInt(Transaction::getValue).forEach(System.out::println);

        System.out.println("------------------------------");
        //7. 所有交易中，最高的交易额是多少
        int max = transactions.stream().mapToInt(Transaction::getValue).max().orElse(-1);
        System.out.println(max);

        System.out.println("------------------------------");
        //8. 找到交易额最小的交易
        int min = transactions.stream().mapToInt(Transaction::getValue).min().orElse(Integer.MAX_VALUE);
        System.out.println(min);




    }

}
