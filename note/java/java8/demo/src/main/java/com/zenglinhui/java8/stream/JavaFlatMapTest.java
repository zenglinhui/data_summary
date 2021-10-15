package com.zenglinhui.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zenglh
 * @date 2021/10/13 9:26
 */
public class JavaFlatMapTest {

    public static void main(String[] args) {

        // 给定一个数字列表，返回一个由每个数的平方构成的列表
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream().map(n -> n * n).collect(Collectors.toList());
        squares.forEach(System.out::println);

        // 给定两个数字列表，返回所有的数对。如：给定列表[1, 2, 3]和[3, 4]，返回[(1, 3),(1, 4),(2, 3),(2, 4),(3, 3),(3, 4)]
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream().flatMap(n -> numbers2.stream().map(nn -> new int[]{n, nn}))
                .collect(Collectors.toList());
        System.out.println("pairs size: " + pairs.size());
        pairs.forEach(p -> System.out.println(p[0] + "," + p[1]));

        System.out.println("-------------------------");

        // 接上一题，只返回能被3整除的数对
        List<Integer> numbers3 = Arrays.asList(1, 2, 3);
        List<Integer> numbers4 = Arrays.asList(3, 4);
        List<int[]> pairs2 = numbers3.stream().flatMap(n3 -> numbers4.stream().
                filter(n4 -> (n3 + n4) % 3 == 0).map(n4 -> new int[]{n3, n4})).
                collect(Collectors.toList());
        pairs2.forEach(p -> System.out.println(p[0] + "," + p[1]));

    }

}
