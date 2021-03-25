# 前言
这些GC日志相关的配置都是基于JDK8，8以后的版本的一些参数配置可能不太一样，这里放一个JDK11的参数配置```java -Xms512m -Xmx512m -Xlog:gc*=info:file=gc.log.time:filecount=0 GCLogAnalysis```，这里还给出一个在线分析gclog文件的网站，有兴趣的可以上去看看 ```https://gceasy.io```
# CMS GC日志分析
启动参数设置为：
```
-XX:+PrintGCDetails
-Xloggc:gc.demo.log
-XX:+PrintGCDateStamps
-Xms512m
-Xmx512m
-XX:+UseConcMarkSweepGC
```
然后来分析一下打印的GC日志，最开始的是虚拟机相关的信息，以及内存相关的信息，这里就不重复介绍了。因为 CMS 为并发标记清除垃圾收集器，设计的目地是为了避免在老年代GC时出现长时间的停顿，默认情况下，使用的并发线程数等于CPU内核数的 1/4。下面可以看到打印的日志比较多，这里还只有一部分的日志，因为 CMS 分了阶段，下面就来分析一下
```
CommandLine flags: 
-XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=178958336
-XX:MaxTenuringThreshold=6 -XX:NewSize=178958336 -XX:OldPLABSize=16 
-XX:OldSize=357912576 -XX:+PrintGC -XX:+PrintGCDateStamps 
-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers 
-XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation 
-XX:+UseParNewGC
2021-03-10T16:56:50.686+0800: 0.693: 
[GC (Allocation Failure) 2021-03-10T16:56:50.686+0800: 0.693: 
[ParNew: 139776K->17469K(157248K), 0.0066339 secs] 434643K->342073K(506816K), 0.0067025 secs] 
[Times: user=0.16 sys=0.00, real=0.01 secs] 
2021-03-10T16:56:50.692+0800: 0.700: 
[GC (CMS Initial Mark) [1 CMS-initial-mark: 324604K(349568K)] 344982K(506816K), 0.0003371 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.693+0800: 0.701: 
[CMS-concurrent-mark-start]
2021-03-10T16:56:50.694+0800: 0.702: 
[CMS-concurrent-mark: 0.001/0.001 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.694+0800: 0.702: 
[CMS-concurrent-preclean-start]
2021-03-10T16:56:50.694+0800: 0.702: 
[CMS-concurrent-preclean: 0.000/0.000 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.694+0800: 0.702: 
[CMS-concurrent-abortable-preclean-start]
2021-03-10T16:56:50.694+0800: 0.702: 
[CMS-concurrent-abortable-preclean: 0.000/0.000 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.694+0800: 0.702: 
[GC (CMS Final Remark) 
[YG occupancy: 36594 K (157248 K)]
2021-03-10T16:56:50.694+0800: 0.702: 
[Rescan (parallel) , 0.0005141 secs]
2021-03-10T16:56:50.695+0800: 0.703: 
[weak refs processing, 0.0000117 secs]
2021-03-10T16:56:50.695+0800: 0.703: 
[class unloading, 0.0002209 secs]
2021-03-10T16:56:50.695+0800: 0.703: 
[scrub symbol table, 0.0003169 secs]
2021-03-10T16:56:50.696+0800: 0.703: 
[scrub string table, 0.0000931 secs]
[1 CMS-remark: 324604K(349568K)] 361198K(506816K), 0.0012410 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.696+0800: 0.703: 
[CMS-concurrent-sweep-start]
2021-03-10T16:56:50.696+0800: 0.704: 
[CMS-concurrent-sweep: 0.001/0.001 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-10T16:56:50.696+0800: 0.704: 
[CMS-concurrent-reset-start]
2021-03-10T16:56:50.697+0800: 0.704: 
[CMS-concurrent-reset: 0.000/0.000 secs] 
[Times: user=0.00 sys=0.00, real=0.00 secs] 
```
这里要注意一下 CommandLine flags 里面，我们指定的GC为 CMS，最后还有一个 -XX:+UseParNewGC 参数，这表示新生代使用的是 ParNew GC.
(1). Minor GC 日志内容：
```
2021-03-10T16:56:50.686+0800: 0.693: 
[GC (Allocation Failure) 2021-03-10T16:56:50.686+0800: 0.693: 
[ParNew: 139776K->17469K(157248K), 0.0066339 secs] 434643K->342073K(506816K), 0.0067025 secs] 
[Times: user=0.16 sys=0.00, real=0.01 secs] 
```
第一行表示发生GC事件的开始时间点，+0800 表示当前时区，0.446：表示这次GC事件对于JVM启动时间的间隔，单位是秒。
第二行的 GC 用来区分 Minor GC、Major GC 还是 Full GC, GC 表示这是 Minor GC(年轻代GC)。 Allocation Failure 表示触发这次GC的原因，是因为对象分配失败，年轻代中没有空间来存放新生成的对象引起GC的。
第三行的 ParNew 表示垃圾收集器的名称，这个名称的意思是：年轻代使用的并行、标记-复制垃圾收集器，专门配合 CMS 垃圾收集器使用。ParNew 用来收集年轻代，CMS 用来收集老年代139776K->17469K(157248K) 表示垃圾收集之前和之后的使用量，括号里面表示年轻代的总空间大小。如果要算GC之后的使用率，后面的数除以年轻代的总大小就行了。0.0066339 secs 表示的是执行的时间。434643K->342073K(506816K) 表示垃圾收集之前和收集之后的整个堆内存的使用情况，括号中的表示堆内存可以的总大小，0.0067025 secs 表示执行时间，单位秒。
第四行表示的是此次GC事件执行的时间，主要通过三个部分来衡量：user 表示所有GC线程消耗的CPU时间; sys 表示系统调用和系统等待消耗的时间; real 表示程序暂停的时间。因为并行的垃圾收集器(ParNew GC)只使用了多线程，所以这里 real ~ (user + sys) / GC线程数，我本机是6核12线程的处理器，光看这一次的话，GC线程数差不多有16个了，但一般情况下，只有6-7个线程左右，从JVM这个默认参数可以看出来 "-XX:MaxTenuringThreshold=6"。程序暂停的时间为 0.01 就是10毫秒。
根据上面的分析，我们需要关注的主要是两个数据：GC暂停时间，以及GC之后的内存使用率。10毫秒的暂停时间还是可以接受的，
我们再来看看内存使用率是怎么算出来的，在上面可以看出来，在这次GC之前堆内存的使用量为 434643K，年轻代使用了 139776K，可以算出来老年代用了 294867K。在GC之后年轻代的使用量为 17469K，年轻代的量下降了 122307K。堆内存的总使用量为 342073K， 总使用量下降了 92570k，那说明有一部分的对象晋升到了老年代，可以算出来有 29737k。那么堆里面现在有的老年代大小为 342073K - 17469K = 324604k, 那么增加的老年代大小也可以用 324604k(现有老年代大小) - 294867K(GC前老年代大小，用总堆大小减掉年轻代大小) = 29737k。这两种算出来的结果应该是一样的。
那么这些数字算出来之后，就可以来算使用率了，这里主要说一下老年代的使用率，用堆内存-年轻代内存(括号内的值) 506816K - 157248K = 349568k 算出老年代的大小，用堆内存使用大小-年轻代内存使用大小 342073K - 17469K = 324604K 算出老年代使用的大小，然后这两数相除就是老年代的使用率了 324604k / 349568k = 93%，看数据应该是会发生一次 Full GC，那下面来看一下 Full GC 的日志  
(2). Full GC 日志内容：
```
1 2021-03-10T16:56:50.692+0800: 0.700: 
2 [GC (CMS Initial Mark) [1 CMS-initial-mark: 324604K(349568K)] 344982K(506816K), 0.0003371 secs] 
3 [Times: user=0.00 sys=0.00, real=0.00 secs] 
4 2021-03-10T16:56:50.693+0800: 0.701: 
5 [CMS-concurrent-mark-start]
6 2021-03-10T16:56:50.694+0800: 0.702: 
7 [CMS-concurrent-mark: 0.001/0.001 secs] 
8 [Times: user=0.00 sys=0.00, real=0.00 secs] 
9 2021-03-10T16:56:50.694+0800: 0.702: 
10 [CMS-concurrent-preclean-start]
11 2021-03-10T16:56:50.694+0800: 0.702: 
12 [CMS-concurrent-preclean: 0.000/0.000 secs] 
13 [Times: user=0.00 sys=0.00, real=0.00 secs] 
14 2021-03-10T16:56:50.694+0800: 0.702: 
15 [CMS-concurrent-abortable-preclean-start]
16 2021-03-10T16:56:50.694+0800: 0.702: 
17 [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] 
18 [Times: user=0.00 sys=0.00, real=0.00 secs] 
19 2021-03-10T16:56:50.694+0800: 0.702: 
20 [GC (CMS Final Remark) 
21 [YG occupancy: 36594 K (157248 K)]
22 2021-03-10T16:56:50.694+0800: 0.702: 
23 [Rescan (parallel) , 0.0005141 secs]
24 2021-03-10T16:56:50.695+0800: 0.703: 
25 [weak refs processing, 0.0000117 secs]
26 2021-03-10T16:56:50.695+0800: 0.703: 
27 [class unloading, 0.0002209 secs]
28 2021-03-10T16:56:50.695+0800: 0.703: 
29 [scrub symbol table, 0.0003169 secs]
30 2021-03-10T16:56:50.696+0800: 0.703: 
31 [scrub string table, 0.0000931 secs]
32 [1 CMS-remark: 324604K(349568K)] 361198K(506816K), 0.0012410 secs] 
33 [Times: user=0.00 sys=0.00, real=0.00 secs] 
34 2021-03-10T16:56:50.696+0800: 0.703: 
35 [CMS-concurrent-sweep-start]
36 2021-03-10T16:56:50.696+0800: 0.704: 
37 [CMS-concurrent-sweep: 0.001/0.001 secs] 
38 [Times: user=0.00 sys=0.00, real=0.00 secs] 
39 2021-03-10T16:56:50.696+0800: 0.704: 
40 [CMS-concurrent-reset-start]
41 2021-03-10T16:56:50.697+0800: 0.704: 
42 [CMS-concurrent-reset: 0.000/0.000 secs] 
43 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
上面是 CMS Full GC 的日志，有点长，因为 CMS 在对老年代进行垃圾收集时分不同的阶段，每个阶段都有自己的日志。在实际的运行中，CMS 在进行老年代并发垃圾收集时，还会有多次的 Minor GC，所以 Full GC 日志中可能会有多次的 Minor GC 日志。至于这里会出现这种情况，可能是因为CMS的GC线程和应用程序的线程一起并发运行，还会有对象分配，所以 Minor GC 还是会发生。下面来依次分析这些阶段。
第一阶段：CMS-initial-mark(初始标记)
这个阶段会 STW 暂停，标记所有的根对象，包括 GC ROOT 直接引用的对象，以及被年轻代中所有存活对象所引用的对象。
现在来分析一下这一阶段的日志：
```
1 2021-03-10T16:56:50.692+0800: 0.700: 
2 [GC (CMS Initial Mark) [1 CMS-initial-mark: 324604K(349568K)] 344982K(506816K), 0.0003371 secs] 
3 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
1 表示这次事件发生的时间，跟前面介绍的一样。
2 表示这次阶段为 "Initial Mark"，标记所有 GC Root。324604K(349568K) 表示老年代的使用量、老年代的空间大小，344982K(506816K) 表示当前堆的使用量和大小，0.0003371 secs 表示这次标记的时间，时间灰常短，只有0.3毫秒，因为要标记的 Root 数量很少。
这里突然想到一个问题，就是年轻代和老年代判断垃圾对象的问题，最开始的方式是用的引用计数，因为引用计数会有循环引用的问题，造成内存泄漏、从而变成内存溢出，所以现在改成可达性分析，什么是可达性分析？就是上面提到的 GC Root 能关联到的对象，也叫对象引用。那么问题来了，年轻代和老年代在标记对象时，用什么方式去标记效率才是最好的？这里给出我的想法：如果是年轻代，大部分对象是朝生夕灭的，所以标记存活对象是最好的;如果是老年代，只有小部分对象才需要清理，所以标记不需要的对象是最好的(但是老年代实际上也是标记存活的对象)。
3 表示这次标记事件的发生的秒数，user 表示GC线程所用的时间，sys 表示系统调用用的时间，real 表示应用程序暂停的时间。这里可以看到这个标记时间可以忽略。
第二阶段：Concurrent Mark(并发标记)
在并发标记阶段，从前一阶段 "Initial Mark" 找到的 ROOT 开始，遍历老年代并标记所有存活对象，这个阶段是并发执行的。所谓并发执行就是 GC 线程和应用线程一起执行。
```
4 2021-03-10T16:56:50.693+0800: 0.701: 
5 [CMS-concurrent-mark-start]
6 2021-03-10T16:56:50.694+0800: 0.702: 
7 [CMS-concurrent-mark: 0.001/0.001 secs] 
8 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
5 表示这是并发标记阶段。
7 表示这次并发标记用的时间，0.001/0.001 secs 单位秒， 分别是 GC 线程消耗时间和实际消耗时间。
8 表示并发标记 user(GC线程使用的时间)、sys(系统用的时间)、real(程序暂停时间)，因为是并发执行，这段时间应用程序也在执行，所以这个时间没多大意义。
第三阶段：Concurrent Preclean(并发预清理)
和应用线程并发执行，不用STW。
```
9 2021-03-10T16:56:50.694+0800: 0.702: 
10 [CMS-concurrent-preclean-start]
11 2021-03-10T16:56:50.694+0800: 0.702: 
12 [CMS-concurrent-preclean: 0.000/0.000 secs] 
13 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
10 表示这是预清理阶段日志，这个阶段会统计前面的并发标记阶段执行过程中发生改变的对象。
12 表示这个阶段所用的时间，分别是GC线程运行时间和实际占用的时间。
13 跟并发标记阶段表示的意义一样，因为是并发执行，没多大意义。
第四阶段：Concurrent Abortable Preclean(可取消的并发预清理)
这个阶段尽可能的多预清理一些，一直循环，直到某一个退出条件满足，就退出。这个阶段完成的工作对 "Final Remark" 的 STW 停顿时间有较大的影响。
```
14 2021-03-10T16:56:50.694+0800: 0.702: 
15 [CMS-concurrent-abortable-preclean-start]
16 2021-03-10T16:56:50.694+0800: 0.702: 
17 [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] 
18 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
第五阶段：Final Remark(最终标记)
最终标记是这次GC中的第二次，也是最后一次的STW停顿。本阶段的目标是完成老年代中所有存活对象的标记。因为之前的预清理阶段是并发执行的，有可能GC线程跟不上应用程序的修改速度，所以需要一次STW暂停来处理各种复杂情况。等执行完最终标记阶段，老年代中所有存活对象都被标记上了，后续就是把不使用的对象清理掉，回收老年代的空间。
```
19 2021-03-10T16:56:50.694+0800: 0.702: 
20 [GC (CMS Final Remark) 
21 [YG occupancy: 36594 K (157248 K)]
22 2021-03-10T16:56:50.694+0800: 0.702: 
23 [Rescan (parallel) , 0.0005141 secs]
24 2021-03-10T16:56:50.695+0800: 0.703: 
25 [weak refs processing, 0.0000117 secs]
26 2021-03-10T16:56:50.695+0800: 0.703: 
27 [class unloading, 0.0002209 secs]
28 2021-03-10T16:56:50.695+0800: 0.703: 
29 [scrub symbol table, 0.0003169 secs]
30 2021-03-10T16:56:50.696+0800: 0.703: 
31 [scrub string table, 0.0000931 secs]
32 [1 CMS-remark: 324604K(349568K)] 361198K(506816K), 0.0012410 secs] 
33 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
20 CMS Final Remark 表示这是最终标记阶段，会标记老年代中所有的存活对象，包括此前的并发标记过程中有变化(创建/修改)的引用。
21 YG occupancy: 36594 K (157248 K) 表示当前年轻代的使用量和总容量。
23 Rescan (parallel) , 0.0005141 secs 在程序暂停后进行重新扫描(Rescan)，以完成存活对象的标记。这是并行执行的(parallel)，消耗的时间是 0.0005141 secs 秒。
25 weak refs processing, 0.0000117 secs 第一个子阶段：处理弱引用的时间。
27 class unloading, 0.0002209 secs 第二个子阶段：卸载不使用的类时间。
29 scrub symbol table, 0.0003169 secs 第三个子阶段：清理符号表，即对应class的metadata中的符号表(symbol tables)。
31 scrub string table, 0.0000931 secs 第四个子阶段：清理内联字符串对应的 string tables。
32 [1 CMS-remark: 324604K(349568K)] 361198K(506816K), 0.0012410 secs 此阶段完成后老年代的使用量和总容量、以及整个堆内存的使用量和总容量。
33 Times: user=0.00 sys=0.00, real=0.00 secs 这次CMS GC 持续的时间。
第六阶段：Concurrent Sweep(并发清除)
这个阶段是和应用线程一起执行，来删除不再使用的对象，回收内存空间。
```
34 2021-03-10T16:56:50.696+0800: 0.703: 
35 [CMS-concurrent-sweep-start]
36 2021-03-10T16:56:50.696+0800: 0.704: 
37 [CMS-concurrent-sweep: 0.001/0.001 secs] 
38 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
37 CMS-concurrent-sweep 0.001/0.001 secs 表示并发清除老年代中所有未被标记的对象。0.001/0.001 secs 表示持续时间和实际占用时间，这个值会四会五入，精确到小数点后3位。
第七阶段：Concurrent Reset(并发重置)
此阶段与应用程序线程并发执行，重置CMS算法相关的内部数据结构，下一次触发GC时就可以直接使用。
```
39 2021-03-10T16:56:50.696+0800: 0.704: 
40 [CMS-concurrent-reset-start]
41 2021-03-10T16:56:50.697+0800: 0.704: 
42 [CMS-concurrent-reset: 0.000/0.000 secs] 
43 [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
42 CMS-concurrent-reset: 0.000/0.000 secs 表示重置CMS算法的内部数据结构，为下一次GC循环做准备。后面是这个阶段持续时间和实际占用的时间。
# 总结
CMS执行完之后，没有直观的地址可以看老年代的使用量，只能用两次 Minor GC 来推算，然后根据(总堆内存的使用量-年轻代的使用量) / (总堆内存-年轻代内存) = 老年代使用率(取值为第二次GC之前的值)，如果使用率比较高的话，说明内存分配小了，那就需要加大内存。从串行 GC、并行 GC、到 CMS，垃圾收集器一直在减少停顿时间做了很多工作，CMS 有很大一部分阶段是可以和应用线程并发执行的，减少每次暂停的时间。CMS 也有一些缺点，最主要的是老年代内存碎片的问题，在堆内存较大的情况下，暂停时间是不可预测的。下一篇会分析一下G1的日志。













