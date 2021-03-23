# 前言
这些GC日志相关的配置都是基于JDK8，8以后的版本的一些参数配置可能不太一样，这里放一个JDK11的参数配置```java -Xms512m -Xmx512m -Xlog:gc*=info:file=gc.log.time:filecount=0 GCLogAnalysis```，这里还给出一个在线分析gclog文件的网站，有兴趣的可以上去看看 ```https://gceasy.io```
# G1 GC日志分析
启动参数设置为：
```
-XX:+PrintGCDetails
-Xloggc:gc.demo.log
-XX:+PrintGCDateStamps
-Xms512m
-Xmx512m
-XX:+UseG1GC
```
G1GC的日志可能会比较多，我电脑上运行有3千多行，不像其它的GC,不超过100行。这里只是个大概值，每个人电脑上跑出来的日志可能不一样。日志太多，下面会截取一部分的日志来分析(两个young GC，一个 mixed GC)。
![pic\GCLogAnalysis3.png](pic\GCLogAnalysis3.png)
```
Java HotSpot(TM) 64-Bit Server VM (25.212-b10) for windows-amd64 JRE (1.8.0_212-b10), built on Apr  1 2019 22:50:23 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 16637352k(9807532k free), swap 26598824k(16112660k free)
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation 
2021-03-15T17:49:07.952+0800: 0.358: [GC pause (G1 Evacuation Pause) (young), 0.0062950 secs]
   [Parallel Time: 5.6 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 358.0, Avg: 358.1, Max: 358.2, Diff: 0.2]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.1, Sum: 0.8]
      [Update RS (ms): Min: 0.1, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 1.0]
         [Processed Buffers: Min: 0, Avg: 1.5, Max: 2, Diff: 2, Sum: 15]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 5.2, Avg: 5.2, Max: 5.3, Diff: 0.2, Sum: 52.2]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 1.2]
         [Termination Attempts: Min: 1, Avg: 1.4, Max: 2, Diff: 1, Sum: 14]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 5.4, Avg: 5.5, Max: 5.6, Diff: 0.2, Sum: 55.3]
      [GC Worker End (ms): Min: 363.6, Avg: 363.6, Max: 363.6, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   [Other: 0.5 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.2 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 101.0M(101.0M)->0.0B(242.0M) Survivors: 11.0M->14.0M Heap: 241.5M(512.0M)->152.3M(512.0M)]
 [Times: user=0.02 sys=0.13, real=0.01 secs] 
2021-03-15T17:49:08.006+0800: 0.412: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0107640 secs]
   [Parallel Time: 9.4 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 411.8, Avg: 411.8, Max: 411.9, Diff: 0.1]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.8]
      [Update RS (ms): Min: 0.1, Avg: 0.1, Max: 0.2, Diff: 0.1, Sum: 1.0]
         [Processed Buffers: Min: 0, Avg: 1.6, Max: 2, Diff: 2, Sum: 16]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 8.9, Avg: 9.0, Max: 9.1, Diff: 0.2, Sum: 89.8]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 1.0]
         [Termination Attempts: Min: 1, Avg: 1.4, Max: 2, Diff: 1, Sum: 14]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 9.2, Avg: 9.3, Max: 9.3, Diff: 0.1, Sum: 92.9]
      [GC Worker End (ms): Min: 421.1, Avg: 421.1, Max: 421.1, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.4 ms]
   [Other: 0.9 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.4 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.1 ms]
      [Free CSet: 0.1 ms]
   [Eden: 187.0M(242.0M)->0.0B(157.0M) Survivors: 14.0M->32.0M Heap: 382.5M(512.0M)->211.5M(512.0M)]
 [Times: user=0.00 sys=0.00, real=0.01 secs] 
2021-03-15T17:49:08.017+0800: 0.423: [GC concurrent-root-region-scan-start]
2021-03-15T17:49:08.017+0800: 0.423: [GC concurrent-root-region-scan-end, 0.0005681 secs]
2021-03-15T17:49:08.017+0800: 0.423: [GC concurrent-mark-start]
2021-03-15T17:49:08.018+0800: 0.424: [GC concurrent-mark-end, 0.0008918 secs]
2021-03-15T17:49:08.018+0800: 0.424: [GC remark 2021-03-15T17:49:08.018+0800: 0.424: [Finalize Marking, 0.0002256 secs] 2021-03-15T17:49:08.019+0800: 0.424: [GC ref-proc, 0.0000637 secs] 2021-03-15T17:49:08.019+0800: 0.424: [Unloading, 0.0006521 secs], 0.0014288 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-15T17:49:08.020+0800: 0.426: [GC cleanup 228M->228M(512M), 0.0005037 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-15T17:49:08.043+0800: 0.449: [GC pause (G1 Evacuation Pause) (young) (to-space exhausted), 0.0069806 secs]
   [Parallel Time: 5.6 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 448.8, Avg: 448.9, Max: 449.0, Diff: 0.2]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.8]
      [Update RS (ms): Min: 0.1, Avg: 0.2, Max: 0.3, Diff: 0.1, Sum: 1.8]
         [Processed Buffers: Min: 0, Avg: 2.9, Max: 4, Diff: 4, Sum: 29]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 4.9, Avg: 5.0, Max: 5.1, Diff: 0.2, Sum: 50.1]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.5]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 10]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 5.3, Avg: 5.4, Max: 5.4, Diff: 0.2, Sum: 53.5]
      [GC Worker End (ms): Min: 454.2, Avg: 454.2, Max: 454.3, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   [Other: 1.3 ms]
      [Evacuation Failure: 0.8 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.2 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 157.0M(157.0M)->0.0B(5120.0K) Survivors: 32.0M->24.0M Heap: 409.7M(512.0M)->326.2M(512.0M)]
 [Times: user=0.00 sys=0.00, real=0.01 secs] 
2021-03-15T17:49:08.051+0800: 0.457: [GC pause (G1 Evacuation Pause) (mixed), 0.0038469 secs]
   [Parallel Time: 3.1 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 456.7, Avg: 456.7, Max: 456.8, Diff: 0.1]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.8]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]
         [Processed Buffers: Min: 0, Avg: 1.2, Max: 3, Diff: 3, Sum: 12]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 2.9, Avg: 2.9, Max: 3.0, Diff: 0.1, Sum: 29.1]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 10]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 3.0, Avg: 3.1, Max: 3.1, Diff: 0.1, Sum: 30.8]
      [GC Worker End (ms): Min: 459.8, Avg: 459.8, Max: 459.8, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.2 ms]
   [Other: 0.5 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.2 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 5120.0K(5120.0K)->0.0B(85.0M) Survivors: 24.0M->1024.0K Heap: 331.2M(512.0M)->312.2M(512.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs]  
```
(1). Evacuation Pause: young(纯年轻代模式转移暂停)
当年轻代空间满了之后，应用线程会被暂停，年轻代内存块中的存活对象被拷贝到存活区。如果还没有存活区，则任意选择一部分空闲的内存块作为存活区。
拷贝的过程称为转移(Evacuation)，这个和其它年轻代收集器工作原理一样。每一段日志信息都比较长，这里会去掉一些后面再分析的日志，下面是抽取了一部分 Evacuation 的日志：
```
1 2021-03-15T17:49:07.952+0800: 0.358: 
2 [GC pause (G1 Evacuation Pause) (young), 0.0062950 secs]
3   [Parallel Time: 5.6 ms, GC Workers: 10]
      ......
4   [Code Root Fixup: 0.0 ms]
5   [Code Root Purge: 0.0 ms]
6   [Clear CT: 0.1 ms]
7   [Other: 0.5 ms]
      ......
8   [Eden: 101.0M(101.0M)->0.0B(242.0M) Survivors: 11.0M->14.0M Heap: 241.5M(512.0M)->152.3M(512.0M)]
9 [Times: user=0.02 sys=0.13, real=0.01 secs] 
```
第一行表示发生GC事件的开始时间点，+0800 表示当前时区，0.358：表示这次GC事件对于JVM启动时间的间隔，单位是秒。
第二行的 [GC pause (G1 Evacuation Pause) (young), 0.0062950 secs] 表示 G1 转移暂停，纯年轻代模式，只清理年轻代空间，一共用了 0.0062950 secs 秒，也就是 6.2ms。
第三行的  [Parallel Time: 5.6 ms, GC Workers: 10] 表示后面的活动由10个 Worker 线程并行执行，消耗时间为 5.6ms(真实的时间)。这里的 Worker 表示一种模式，类似于一个老板指挥多个人干活。
第四行 [Code Root Fixup: 0.0 ms] 表示释放用于管理并行活动的内部数据，一般时间接近于0，因为这个可以并行执行的。
第五行 [Code Root Purge: 0.0 ms] 表示清理其他部分数据，时间基本上也是0，因为也是并行的。
第七行 [Other: 0.5 ms] 表示其它活动消耗的时间，大部分是并行执行。
第八行 [Eden: 101.0M(101.0M)->0.0B(242.0M) Survivors: 11.0M->14.0M Heap: 241.5M(512.0M)->152.3M(512.0M)] ``` Eden: 101.0M(101.0M)->0.0B(242.0M) ``` 表示暂停之前和暂停之后，Eden 区的使用量(总容量);```Survivors: 11.0M->14.0M``` 表示GC暂停前后，存活区的使用量;```Heap: 241.5M(512.0M)->152.3M(512.0M)```
暂停前后，整个堆内存的使用量(总容量)。
第九行 [Times: user=0.02 sys=0.13, real=0.01 secs] 表示这次GC花费的时间，user 线程用的时间，sys 系统用的时间，real 程序暂停的时间。
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
这里突然想到一个问题，就是年轻代和老年代判断垃圾对象的问题，最开始的方式是用的引用计数，因为引用计数会有循环引用的问题，造成内存溢出、从而变成内存泄漏，所以现在改成可达性分析，什么是可达性分析？就是上面提到的 GC Root 能关联到的对象，也叫对象引用。那么问题来了，年轻代和老年代在标记对象时，用什么方式去标记效率才是最好的？这里给出我的想法：如果是年轻代，大部分对象是朝生夕灭的，所以标记存活对象是最好的;如果是老年代，只有小部分对象才需要清理，所以标记不需要的对象是最好的(但是老年代实际上也是标记存活的对象)。
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













