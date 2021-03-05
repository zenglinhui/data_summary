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
    ParOldGen 老年代总计 648192K，用了 365847K
    其中 对象空间 648192K，用了 56% used
    ```
    ParOldGen       total 648192K, used 365847K
        object space 648192K, 56% used
    ```
    Metaspace 元数据区总计使用了 3795K，容量是 4540K，JVM 保证可用的大小是 4864K，保留空间 1GB(1056768K) 左右
    其中 类空间 使用了 419K，容量 428K， 保证可用大小是 512K
    ```
    Metaspace       used 3795K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 419K, capacity 428K, committed 512K, reserved 1048576K
    ```
(4). -XX:+ 这是参数的格式，一个布尔值的开关
3. 指定GC日志文件
(1). 加上参数 ```-Xloggc:gc.demo.log``` 启动时指定JVM的GC日志文件，JDK8 开始，可以使用 ```%p```, ```%t```等占位符来指定GC输出文件，分别表示进程 pid 和启动时间戳
(2). 加上参数执行后在控制台不会输出相应GC的信息，在当前目录下会生成一个 ```gc.demo.log``` 的文件(IDEA里面在根目录下)，也可指定路径存放日志文件，如: ```-Xloggc:/var/log/gc.demo.log```
(3). 文件中的信息会比打印到控制台中的信息会多一点，虚拟机的版本、内存页大小、物理内存和使用的内存、swap,CommandLine flags 这一行表示线上虚拟机的配置，可以减少分析排查问题时的时间。之前以为JDK8默认的垃圾收集器是CMS，在这里看来是 ```-XX:+UseParallelGC```
```
Java HotSpot(TM) 64-Bit Server VM (25.212-b10) for windows-amd64 JRE (1.8.0_212-b10), built on Apr  1 2019 22:50:23 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 16637352k(6557032k free), swap 31841704k(14750784k free)
CommandLine flags: -XX:InitialHeapSize=266197632 -XX:MaxHeapSize=4259162112 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
```
(4). 在GC的每一行前面都加上了时间，如```0.175:```,这里是因为在指定```-Xloggc:```时，自动加上了```-XX:+PrintGCTimeStamps``` 这个参数，这个参数表示的意思是JVM启动后经过的时间(单位秒)
```
0.175: [GC (Allocation Failure) [PSYoungGen: 64576K->10740K(75776K)] 64576K->24649K(249344K), 0.0039773 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.188: [GC (Allocation Failure) [PSYoungGen: 75503K->10738K(140800K)] 89412K->47492K(314368K), 0.0042977 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
4. 打印GC事件发生日期和时间
(1). 在上面我们可以知道，加上```-Xloggc:```这个参数时，会自动加上```-XX:+PrintGCTimeStamps``` 这个参数，会打印出JVM启动后经过的时间。现在指定日期参数 ```-XX:+PrintGCDateStamps```会打印出当前的年月日时分秒+启动后经过的时间
pic\java\jvm\GC日志分析\GCLogAnalysis2.png
5. 指定堆内存大小
(1). 在 CommandLine flags 中有两个参数 ```-XX:InitialHeapSize=266197632 -XX:MaxHeapSize=4259162112```等价于 ```-Xms256m -Xmx4g```，上面是没有指定堆大小时的默认值，如果有需要可以手动指定
(2). 在指定```-Xms512m -Xmx512m``` 这两个参数时，GC日志文件中会打印出下面这两个值，说明已经起作用了，还有一个现象是 Full GC 的次数多了，说明指定的最大堆内存比较小，以后在生产环境查找问题这是一个参考点
```CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912
```
5. 指定垃圾收集器
(1). 在JDK8里面我们可以使用下面几种垃圾收集器
```
-XX:+UseSerialGC    #串行GC
-XX:+UseParallelGC  #并行GC
-XX:+UseParallelGC -XX:+UseParallelOldGC    #跟上面是等价的GC
-XX:+UseConcMarkSweepGC     #CMS GC
-XX:+UseConcMarkSwaeepGC -XX:+UseParNewGC   #跟上面是等价的GC
-XX:+UseG1GC        #G1 GC
```
(2). 这里要注意的是CMS垃圾收集器，指定CMS时，命令行中的参数会有 ```-XX:MaxNewSize=178958336 -XX:NewSize=178958336 -XX:OldSize=357912576``` 这几个值，分别代表年轻代的最大值、年轻代的初始值、老年代的初始值。如果指定的收集器是```-XX:+UseParNewGC```,则老年代GC会使用```SerialGC```，这时不会在日志文件中体现，会在控制台打出一句
```
Java HotSpot(TM) 64-Bit Server VM warning: Using the ParNew young collector with the Serial old collector is deprecated and will likely be removed in a future release
```
可以看出来这是个警告，Serial 收集器可能会在未来版本移除，所以不建议在生产指定 ParNewGC。
(3). 在使用G1垃圾收集器时，不需要指定G1垃圾收集器的年轻代大小，因为G1的回收方式是小批量划定区块(region)进行，一次GC中可以能年轻代又有老年代，某个区块可能一会是年轻代，一会又是年轻代。
6. 其它参数
JVM里面的参数有几千个，当然需要我们手动来调优的可能就那么10几个左右，主要掌握重要的参数就差不多了，切记不要过度优化。这里还有日志相关的参数
```
-XX:+PrintGCApplicationStoppedTime  #可以输出每次GC的持续时间和程序暂停时间
-XX:+PrintReferenceGC       #输出GC清理了多少引用类型
```
# GC事件类型简介
1. 垃圾收集事件可以分为三种类型(Garbage Collection events)
(1). Minor GC(小型GC)
(2). Major GC(大型GC)
(3). Full GC(完全GC)
2. Minor GC(小型GC)
收集年轻代内存的GC事件称为 Minor GC，因为清理的是年轻代，又叫年轻代GC(Young GC)
(1). 当JVM无法为新对象分配内存空间时就会触发 Minor GC(一般是Eden区满了)。如果对象的分配速率很快，那么 Minor GC 的次数就会很多，频率也快。
(2). Minor GC 事件不处理老年代，所以会把所有从老年代指向年轻代的引用都当做 GC Root。从年轻代指向老年代的引用则在标记阶段被忽略。
(3). Minor GC 每次都会引起STW(stop-the-world)，挂起所有的应用线程。对于大部分应用程序来说，暂停时间可以忽略不计，因为 Eden 区里面的对象大部分都会回收，基本上不会复制到存活区/老年代，如果不是这种情况，那么新创建的对象就不会能被GC清理，Minor GC 停顿时间就会增大，明显影响GC的性能。
3. Major GC & Full GC
这两个在JVM规范中或者是GC论文中都没有正式定义，除了 Minor GC 外，另外两种GC事件是 
```
Major GC(大型GC)    #清理老年代空间(Old Space)的GC事件
Full GC(完全GC)     #清理整个堆内存空间的GC事件，包括年轻代和老年代空间
```
不管是 Major GC 还是 Full GC，都会造成单次较长时间的STW暂停，所以我们需要关注的是，某次GC事件，暂停了所有线程，对系统造成了什么样的性能影响，是暂停时间过长，还是GC线程与其它业务线程并发执行，暂停时间忽略不计。





 