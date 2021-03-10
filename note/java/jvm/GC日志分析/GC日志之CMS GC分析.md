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
1. 日志开头是系统以及内存的一些信息，JVM的版本、内存页大小、物理内存大小、swap大小，以及JVM的一些参数
2. 上面的日志放了两次GC的事件，第一次是清理了年轻代，第二次是清理了整个堆区，来看一下这两次分别有区别
(1). Minor GC 日志内容：
```
1 2021-03-05T14:32:49.200+0800: 0.446: 
2 [GC (Allocation Failure) 2021-03-05T14:32:49.201+0800: 0.446: 
3 [DefNew: 157247K->17471K(157248K), 0.0166896 secs] 445487K->349954K(506816K), 0.0167582 secs] 
4 [Times: user=0.02 sys=0.00, real=0.02 secs] 
```
第一行表示发生GC事件的开始时间点，+0800 表示当前时区，0.446：表示这次GC事件对于JVM启动时间的间隔，单位是秒。
第二行的 GC 用来区分 Minor GC、Major GC 还是 Full GC, GC 表示这是 Minor GC(年轻代GC)。 Allocation Failure 表示触发这次GC的原因，是因为对象分配失败，年轻代中没有空间来存放新生成的对象引起GC的。
第三行的 DefNew 表示垃圾收集器的名称，这个名称的意思是：年轻代使用的单线程、标记-复制、STW垃圾收集器。157247K->17471K(157248K) 表示垃圾收集之前和之后的使用量，括号里面表示年轻代的总空间大小。如果要算GC之后的使用率，后面的数除以年轻代的总大小就行了。0.0166896 secs 表示的是执行的时间。445487K->349954K(506816K) 表示垃圾收集之前和收集之后的整个堆内存的使用情况，括号中的表示堆内存可以的总大小，0.0167582 secs 表示执行时间，单位秒。
第四行表示的是此次GC事件执行的时间，主要通过三个部分来衡量：user 表示所有GC线程水消耗的CPU时间; sys 表示系统调用和系统等待消耗的时间; real 表示程序暂停的时间。因为品行的垃圾收集器(Serial GC)只使用了单个线程，所以这里 real = user + sys 时间为 0.02 就是20毫秒。
根据上面的分析，我们需要关注的主要是两个数据：GC暂停时间，以及GC之后的内存使用率。20毫秒的暂停对于大部分的系统来说是可以接受的，但是某些延迟比较敏感的系统就不行，比如游戏服务、高频的交易服务，20秒的延迟估计在游戏行业是混不下去的。
我们再来看看内存使用率是怎么算出来的，在上面可以看出来，在这次GC之前堆内存的使用量为 445487K，年轻代使用了 157247K，可以算出来老年代用了 288240K。在GC之后年轻代的使用量为 17471K，年轻代的量下降了 139776K。堆内存的总使用量为 349954K， 总使用量下降了 95533k，那说明有一部分的对象晋升到了老年代，可以算出来有 44243k。那么堆里面现在有的老年代大小为 349954K - 17471K = 332483k, 那么增加的老年代大小也可以用 332483k(现有老年代大小) - 288240K(GC前老年代大小) = 44243k。这两种算出来的结果应该是一样的。
(2). Full GC 日志内容：
```
1 2021-03-05T14:32:49.232+0800: 0.477: 
2 [GC (Allocation Failure) 2021-03-05T14:32:49.232+0800: 0.477: 
3 [DefNew: 157089K->157089K(157248K), 0.0000145 secs]2021-03-05T14:32:49.232+0800: 0.477: 
4 [Tenured: 332483K->275039K(349568K), 0.0312699 secs] 489572K->275039K(506816K), 
5 [Metaspace: 3282K->3282K(1056768K)], 0.0315905 secs] 
6 [Times: user=0.03 sys=0.00, real=0.03 secs] 
```
这里相信大家有一点很疑惑，明明日志上叫GC，为什么我这里会叫 Full GC。之前定义 Major GC(大型GC)和 Full GC(完全GC)时，大型GC是清理老年代空间，完全GC是清理整个堆内存空间，所以从上面的日志上看，这里是清理了整个堆内存空间，所以叫 Full GC。第(3)点要介绍的是GC我会叫 Major GC，因为只清理了老年代
第一行表示开始时间，跟第(1)点一样，就不多说了。
第二行表示因为内存分配失败，发生了一次 Full GC，后面的表示JVM启动到这次GC发生的时间。
第三行表示用了 DefNew 这个收集器，发生了年轻代GC，这里看到 157089K->157089K(157248K) 这个年轻代空间没有变化，时间也只有 0.0000145 secs 秒，说明基本没有处理年轻代。
第四行 Tenured 这个收集器，用来清理老年代空间， 使用的是单线程的STW垃圾收集器，使用的算法为 "标记-清除-整理(mark-sweep-compact)"。332483K->275039K(349568K) 表示老年代在GC前后的使用量以及老年代的大小，0.0312699 secs 表示清理花的时间。489572K->275039K(506816K) 表示整个堆内存的使用情况以及堆空间大小。
第五行 Metaspace 表示元数据空间，3282K->3282K(1056768K) 空间比较大，用量比较小，使用量基本上没变，但是时间还是花了 0.0315905 secs 秒。
第六行表示此次执行GC的时间， real = user + sys 时间为 0.03 就是30毫秒.
根据上面的分析，可以看出来这次 Full GC 比上次的 Minor GC 多了10毫秒，新生代内存没有变化，Metaspace 区也没有什么变化，老年代内存 332483K->275039K(349568K) 降低了 332483K - 275039K = 57444K，使用率为 275039 / 349568 = 78%，说明老年代使用比例比较高了，需要后续观察及优化。
(3). Major GC 日志内容：
```
1 2021-03-05T14:32:49.760+0800: 1.005: 
2 [Full GC (Allocation Failure) 2021-03-05T14:32:49.760+0800: 1.005: 
3 [Tenured: 349363K->349401K(349568K), 0.0402898 secs] 506514K->363601K(506816K), 
4 [Metaspace: 3282K->3282K(1056768K)], 0.0403638 secs] 
5 [Times: user=0.05 sys=0.00, real=0.04 secs]
```
对于这里为什么会叫 Major GC 在第(2)点有介绍，就不重复了，下面来看看这次GC发生了哪些内存操作。
第一行表示开始时间，跟上面是一样的。
第二行表示内存分配失败，发生了一次 GC，后面表示JVM启动到这次GC发生的时间。
第三行表示用 Tenured 收集器，清理了老年代空间 349363K->349401K(349568K) 但这次好像没有清理掉老年代内存，反而增加了 38k 的内存，花费了 0.0402898 secs 40毫秒， Metaspace 区没有变化，也花了40毫秒。
第四行表示此次执行GC的时间，上面基本上都是 real = user + sys，因为是串行GC，所以会认为这个等式成立，但是这里出现了 real < user + sys 这种情况，这里可以分析一下为什么会出现这种情况，user：表示此次垃圾回收，垃圾收集线程消耗的所有 CPU 时间(Total CPU Time)、sys：操作系统调用(OS Call)以及等待系统事件的时间(Waiting for System event)、real：应用程序暂停时间(Clock Time)，串行一般情况下是 real = user + sys; 所以我猜测这里是不是因为算时间时出现了一点误差。




