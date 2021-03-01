# GC日志解读和分析
1. 演示代码在 demo 文件夹下 jvm/GCLogAnalysis.java类中
这里面的代码用来模拟让部分对象晋升到老年代，可以修改持续运行时间和缓存对象个数。另外还可以用作其他用途：
(1). 在限制堆内存的情况下，让缓存对象变多，可以模拟```内存溢出```。
(2). 增加运行时长，就可以用 ```VisualVM``` 等工具来实时监控和观察。
(3). 可以用全局静态变量来缓存，用来模拟 ```内存泄漏```，以及进行堆内存 Dump 的试验和分析。
(4). 加大每次生成的数组大小，可以用来模拟 ```大对象/巨无霸对象```
2. 输出GC日志详情
(1). 在命令行输入 java -XX:+PrintGCDetails GCLogAnalysis
(2). 如果用的是 IDEA 集成工具，可以在启动的配置文件中的 VM options 属性来设置 -XX:+PrintGCDetails 这个参数
pic\java\jvm\GC日志分析\GCLogAnalysis1.png
(3). 图中看 Heap 堆内存使用的情况，里面有 PSYoungGen、ParOldGen、Metaspace 这三个内存区域的情况。
    年轻代总计 1020416K，使用量 221119K
    其中 eden space 占了 823296K，用了 2% used
    其中 from space 占了 197120K， 用了 99% used
    其中 to   space 占了 292864K，用了 0% used
    这里可以看出 from 和 to 区只会有一个使用
    ```
    PSYoungGen total 1020416K, used 221119K
        eden space 823296K, 2% used
        from space 197120K, 99% used
        to   space 292864K, 0% used
    ```
(7). -XX:+ 这是参数的格式，一个布尔值的开关
 