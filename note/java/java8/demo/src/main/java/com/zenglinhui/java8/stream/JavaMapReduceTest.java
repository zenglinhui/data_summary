package com.zenglinhui.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author zenglh
 * @date 2021/10/13 11:19
 */
public class JavaMapReduceTest {

    public static void main(String[] args) {

        //得出列表中元素之和
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        //sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum);

        //得出列表中元素的最大值
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        System.out.println(max.orElse(-1));

        //得到流中有多少个菜
        List<Integer> menu = Arrays.asList(1, 2, 3, 4, 5);
        int count = menu.stream().map(m -> 1).reduce(0, Integer::sum);
        /*long lCount = menu.stream().count();
        lCount = menu.size();*/
        System.out.println(count);

    }

}
