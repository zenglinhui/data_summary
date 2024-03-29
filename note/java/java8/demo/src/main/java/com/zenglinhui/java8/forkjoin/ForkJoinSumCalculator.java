package com.zenglinhui.java8.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * @author zenglh
 * @date 2021/10/15 17:26
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    private final long[] numbers;

    private final int start;

    private final int end;

    public static final long THRESHOLD = 10_000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        int temp = start + length / 2;
        System.out.println("start - end - temp :" + start + "-" + end + "-" + temp);
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, temp);
        leftTask.fork();

        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, temp, end);
        long rightResult = rightTask.compute();
        long leftResult = leftTask.join();
        return leftResult + rightResult;
    }


}
