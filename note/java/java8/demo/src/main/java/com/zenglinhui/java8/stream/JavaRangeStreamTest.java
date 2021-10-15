package com.zenglinhui.java8.stream;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author zenglh
 * @date 2021/10/13 18:27
 */
public class JavaRangeStreamTest {


    public static void main(String[] args) {

        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
        evenNumbers.forEach(System.out::println);

        Stream<String> stream = Stream.of("Java 8", "Lambdas", "In", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);

        // 无限流
        Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);

        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }

}
