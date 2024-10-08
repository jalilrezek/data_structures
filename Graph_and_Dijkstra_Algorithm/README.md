# Directions
I implemented a Graph data structure using Vertices and Edges as supporting classes.

Please see "Graph_Implementations/SparseGraph.java" for the main Graph implementation.

Afterwards, I implemented Dijkstra's algorithm. I tested it, and the results are discussed below.

Please see "Dijkstra_Implementation/DijkstraStreetSearcher.java" for the algorithm's implementation.


## Discussion 

I tested my Graph data structure with Dijkstra's algorithm by using a dataset consisting of various Baltimore City addresses as Vertices and streets connecting them as Edges.

For testing the three paths' memory and runtime performance, here are my observations.
In no case did my algorithm fail to find a path.

The data is below. I made several runs and got a sense of what the averages were. The
runs below reflected them decently well.

I would like to note that when I discuss the implications of a path's length on the 
usage of space and memory, I refer to the number of roads, not the actual length in meters.
This is because the lengths of individual roads are mere doubles that the program adds 
to "totalDist". What makes the program actually do more work in one run than another, is having
to explore more roads. So in terms of runtime and memory performance, it's the number of roads
and not their individual lengths that matter. 

Loading the network is the same task every time, although it did vary quite a bit between runs.
Longer paths as expected took longer to run Dijkstra's algorithm. Going from JHU to Druid Lake
took slightly longer (15 ms) than going from 7-11 to Druid Lake (11 ms) which makes sense
given the path lengths (8818.5187 meters vs 5827.3652 meters, respectively). Indeed, we 
see many more roads being taken in the longer path, which naively suggests it is longer,
although of course what really matter are the lengths of the roads which can vary greatly.

Going from Inner Harbor to JHU is the path with the most roads and takes the greatest time to find the
shortest path, at 25ms. This makes sense since the algorithm has to branch out farther in the
network before it can finalize the "endpoint" (i.e. before it pops it off the queue, certifying
that it has found the shortest path to that vertex). 

In terms of memory usage, loading the network generally took around 9.5kb. We expect everything
up until "finding the shortest path" to be generally similar since that is the same for all
three runs - simply loading in the data, instantiating the Graph and Streetsearcher objects,
and loading the graph with the data. 

The paths with the most roads, as expected, took the most memory. For instance, finding the path from
Inner Harbor to JHU took Δ = 42.367 KB in addition to whatever had been used previously in
loading up the graph, whereas finding the shortest path from 7-11 to Druid Lake took a mere
(Δ = 1.867) KB. In line with this, the path with the second-most roads and second-most 
memory usage, from JHU to Druid Lake, took Δ = 18.906 KB.

The results of my JMH runtime confirm this hypothesis. The results are at the very bottom
of this file. Here is a summary:

JHU to Druid Lake: maximumUsedAfterGC: 414066278.400 ±   221004.501   bytes
                   Time:               3.196 ±        0.256   ms/op 	
7-11 to Druid Lake: maximumUsedAfterGC: 74257373.600 ±  1070525.148   bytes          
                   Time: 			   2.140 ±        0.097   ms/op
Inner Harbor to JHU: maximumUsedAfterGC: 69039041.600 ±  1208302.945   bytes         
                   Time: 			   7.199 ±        0.274   ms/op

As expected, the path with the most roads (Inner Harbor to JHU) took about twice as long
for each operation, in milliseconds.

**Interesting Observation**
The JMH runtime data actually contradicts my hypothesis that spatial complexity correlates with
the number of vertices (which correlates to the number of roads) explored. It seems I was 
correct that having to search over more roads leads to longer runtimes based on the ms/op
scores above, but the spatial complexities are actually rather similar.

I would guess that much of the space used is actually in things besides finding the shortest
path, for instance loading the network and other bookkeeping tasks. So, random variation in those
components which account for most of the used memory, can mask the fact that a path with
fewer roads required less space specifically for finding the shortest path, since the overall
memory required was larger as a result of the idiosyncrasies of that particular run.

**Interesting Observation**
I'd like to note that I had a run where JHU to Druid Lake took 49 Kb, which is more than
from Inner Harbor to JHU. However, in general after doing many runs, the path from
Inner Harbor to JHU took much more memory to find, hovering around 40-60 KB, whereas
the path from JHU to Druid Lake took around 15-35 KB.

In general, however, I would expect the paths with more roads would take longer to find,
and occupy more space in memory. This is because the algorithm has to load more information
for the "findPath" function, has to add more vertices to its auxiliary data structures during
the search, and because of that has to do more loop iterations, increasing both temporal
and spatial complexity. 

Since the algorithm works by blindly pursuing the (guaranteed) shortest path from the source to
the vertex it is currently at, until that vertex just so happens to be the destination vertex,
it makes sense why paths with more roads take more time and space to find; the algorithm
must go over more of the total network, and more information from that network must thus
be added to the various data structures used in the process, like the "distances" HashMap and
the "explored" HashSet.

Below is the data.

**JHU to Druid Lake**

JHU to Druid Lake Driver Data:

Total Distance: 8818.5187
121.60 	45662:
137.15 	40816:
318.90 	40867:N_CHARLES_ST
60.49 	42002:E_33RD_ST
293.14 	8344:3200_BLK_N_CHARLES_ST
318.96 	11147:3200_BLK_N_CHARLES_ST
151.62 	39907:ART_MUSEUM_DR
665.00 	48094:UNIT__BLK_ART_MUSEUM_DR
129.55 	43910:ART_MUSEUM_DR
213.64 	46364:WYMAN_PARK_DR
255.02 	26692:2900_BLK_WYMAN_PARK_DR
42.03 	39554:N_HOWARD_ST
136.52 	26872:200_BLK_W_29TH_ST
146.67 	26712:200_BLK_W_29TH_ST
167.43 	15177:200_BLK_W_29TH_ST
230.86 	11871:200_BLK_W_29TH_ST
196.68 	14691:300_BLK_W_29TH_ST
224.61 	30101:300_BLK_W_29TH_ST
123.84 	5917:300_BLK_W_29TH_ST
79.80 	21125:300_BLK_W_29TH_ST
78.16 	21194:400_BLK_W_29TH_ST
115.90 	17656:400_BLK_W_29TH_ST
600.37 	26121:500_BLK_W_29TH_ST
480.41 	14609:2900_BLK_SISSON_ST
284.62 	23569:700_BLK_WYMAN_PARK_DR
394.12 	18109:800_BLK_WYMAN_PARK_DR
281.96 	31600:900_BLK_WYMAN_PARK_DR
39.03 	33121:900_BLK_WYMAN_PARK_DR
71.18 	34391:1000_BLK_WYMAN_PARK
1160.95 	41471:EAST_DR
190.65 	43386:UNNAMED_ST
1107.65 	41640:

Note: Places where there's no name for a street, like 41640:      , correspond to places in
the actual file where no name is given. It's not an error in my program.

JHU to Druid Lake SystemRuntimeTest info:

#~~~ SystemRuntimeTest ~~~
Config: baltimore.streets.txt from -76.6175,39.3296 to -76.6383,39.3206
Loading network took 92 milliseconds.
Finding shortest path took 15 milliseconds.
#~~~~~~     END     ~~~~~~

JHU to Druid Lake Memory Monitor Test Data:

#~~~~~ MemoryMonitorTest ~~~
Config: baltimore.streets.txt from -76.6175,39.3296 to -76.6383,39.3206
Used memory: 12599.63 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 13729.45 KB (Δ = 1129.828)
Loading the network
Used memory: 23292.38 KB (Δ = 9562.922)
Finding the shortest path
Used memory: 23311.28 KB (Δ = 18.906)
Setting objects to null (so GC does its thing!)
Used memory: 14638.46 KB (Δ = -8672.820)
#~~~~~~     END     ~~~~~~


**7-11 to Druid Lake**

7-11 to Druid Lake Driver Data: 

Config: baltimore.streets.txt from -76.6214,39.3212 to -76.6383,39.3206
Network Loaded: 27598 roads, 9024 endpoints
Total Distance: 5827.3652
397.42 	24509:2800_BLK_REMINGTON_AVE
196.68 	14691:300_BLK_W_29TH_ST
224.61 	30101:300_BLK_W_29TH_ST
123.84 	5917:300_BLK_W_29TH_ST
79.80 	21125:300_BLK_W_29TH_ST
78.16 	21194:400_BLK_W_29TH_ST
115.90 	17656:400_BLK_W_29TH_ST
600.37 	26121:500_BLK_W_29TH_ST
480.41 	14609:2900_BLK_SISSON_ST
284.62 	23569:700_BLK_WYMAN_PARK_DR
394.12 	18109:800_BLK_WYMAN_PARK_DR
281.96 	31600:900_BLK_WYMAN_PARK_DR
39.03 	33121:900_BLK_WYMAN_PARK_DR
71.18 	34391:1000_BLK_WYMAN_PARK
1160.95 	41471:EAST_DR
190.65 	43386:UNNAMED_ST
1107.65 	41640:

7-11 to Druid Lake SystemRuntimeTest Data:

#~~~ SystemRuntimeTest ~~~
Config: baltimore.streets.txt from -76.6214,39.3212 to -76.6383,39.3206
Loading network took 102 milliseconds.
Finding shortest path took 11 milliseconds.
#~~~~~~     END     ~~~~~~

7-11 to Druid Lake MemoryMonitorTest Data: 

#~~~ MemoryMonitorTest ~~~
Config: baltimore.streets.txt from -76.6214,39.3212 to -76.6383,39.3206
	Used memory: 12606.39 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
	Used memory: 13722.65 KB (Δ = 1116.258)
Loading the network
	Used memory: 23288.51 KB (Δ = 9565.859)
Finding the shortest path
	Used memory: 23290.38 KB (Δ = 1.867)
Setting objects to null (so GC does its thing!)
	Used memory: 14618.86 KB (Δ = -8671.516)
#~~~~~~     END     ~~~~~~

**Inner Harbor to JHU**

Inner Harbor to JHU Driver Data: 

Total Distance: 16570.4909
462.64 	20226:100_BLK_SOUTH_ST
163.04 	48137:UNIT__BLK_SOUTH_ST
71.81 	47386:UNIT__BLK_SOUTH_ST
158.76 	47419:UNIT__BLK_SOUTH_ST
271.10 	47548:UNIT__BLK_SOUTH_ST
268.59 	47459:UNIT__BLK_GUILFORD_AVE
343.86 	28621:200_BLK_E_FAYETTE_ST
296.82 	33813:100_BLK_N_CALVERT_ST
134.19 	28959:200_BLK_N_CALVERT_ST
299.37 	24432:200_BLK_N_CALVERT_ST
454.21 	23235:300_BLK_N_CALVERT_ST
147.99 	9313:300_BLK_N_CALVERT_ST
185.36 	5947:400_BLK_N_CALVERT_ST
151.15 	30373:100_BLK_ORLEANS_ST
156.75 	31819:100_BLK_ORLEANS_ST
165.18 	36462:400_BLK_SAINT_PAUL_PL
199.96 	33237:500_BLK_SAINT_PAUL_PL
50.14 	42343:SAINT_PAUL_PL
204.02 	35069:500_BLK_SAINT_PAUL_PL
369.08 	27667:600_BLK_SAINT_PAUL_ST
67.58 	23282:600_BLK_SAINT_PAUL_ST
63.19 	8359:700_BLK_SAINT_PAUL_ST
201.99 	31442:700_BLK_SAINT_PAUL_ST
120.04 	22312:700_BLK_SAINT_PAUL_ST
164.64 	24117:800_BLK_SAINT_PAUL_ST
58.65 	2462:800_BLK_SAINT_PAUL_ST
163.17 	14582:800_BLK_SAINT_PAUL_ST
347.58 	16051:900_BLK_SAINT_PAUL_ST
147.31 	17769:900_BLK_SAINT_PAUL_ST
159.48 	34028:1000_BLK_SAINT_PAUL_ST
341.45 	8766:1000_BLK_SAINT_PAUL_ST
383.17 	1691:1100_BLK_SAINT_PAUL_ST
386.86 	4356:1200_BLK_SAINT_PAUL_ST
338.91 	19656:1300_BLK_SAINT_PAUL_ST
42.19 	40957:SAINT_PAUL_ST
46.30 	44623:SAINT_PAUL_ST
216.46 	39136:RAMP
338.93 	43462:I_83___S
92.13 	45367:I_83___S
107.28 	40942:N_CHARLES_ST
123.62 	40898:N_CHARLES_ST
468.44 	13325:1500_BLK_N_CHARLES_ST
386.86 	4876:1700_BLK_N_CHARLES_ST
212.37 	22851:1800_BLK_N_CHARLES_ST
244.03 	8607:1800_BLK_N_CHARLES_ST
46.58 	20118:1800_BLK_N_CHARLES_ST
382.67 	19448:1900_BLK_N_CHARLES_ST
366.72 	11838:2000_BLK_N_CHARLES_ST
367.72 	1734:2100_BLK_N_CHARLES_ST
376.14 	26449:2200_BLK_N_CHARLES_ST
393.44 	5511:2300_BLK_N_CHARLES_ST
239.67 	18280:2400_BLK_N_CHARLES_ST
99.97 	2083:2400_BLK_N_CHARLES_ST
204.48 	30800:2400_BLK_N_CHARLES_ST
209.45 	28376:2500_BLK_N_CHARLES_ST
75.71 	10673:2500_BLK_N_CHARLES_ST
102.26 	18381:2500_BLK_N_CHARLES_ST
165.69 	25136:2500_BLK_N_CHARLES_ST
467.97 	8403:2600_BLK_N_CHARLES_ST
467.02 	21531:2700_BLK_N_CHARLES_ST
465.66 	21565:2800_BLK_N_CHARLES_ST
231.86 	42406:N_CHARLES_ST
166.74 	41710:N_CHARLES_ST
64.04 	39032:N_CHARLES_ST
240.14 	41330:N_CHARLES_ST
233.97 	44412:N_CHARLES_ST
195.68 	44001:N_CHARLES_ST
205.02 	44656:N_CHARLES_ST
202.15 	42144:N_CHARLES_ST
245.47 	42334:N_CHARLES_ST
318.90 	40867:N_CHARLES_ST
137.15 	40816:
121.60 	45662:


Inner Harbor to JHU SystemRuntimeTest Data:

#~~~ SystemRuntimeTest ~~~
Config: baltimore.streets.txt from -76.6107,39.2866 to -76.6175,39.3296
Loading network took 120 milliseconds.
Finding shortest path took 25 milliseconds.
#~~~~~~     END     ~~~~~~


Inner Harbor to JHU MemoryMonitorTest Data:

#~~~ MemoryMonitorTest ~~~
Config: baltimore.streets.txt from -76.6107,39.2866 to -76.6175,39.3296
	Used memory: 12598.02 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
	Used memory: 13706.85 KB (Δ = 1108.836)
Loading the network
	Used memory: 23258.14 KB (Δ = 9551.289)
Finding the shortest path
	Used memory: 23300.51 KB (Δ = 42.367)
Setting objects to null (so GC does its thing!)
	Used memory: 14616.24 KB (Δ = -8684.266)
#~~~~~~     END     ~~~~~~


**JMH Results** 

# Run complete. Total time: 00:25:33

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                                                   (endPointKey)  Mode  Cnt          Score          Error   Units
JmhRuntimeTest.findShortestPath                                         JHU to Druid Lake  avgt   20          3.196 ±        0.256   ms/op
JmhRuntimeTest.findShortestPath:+c2k.gc.alloc.rate                      JHU to Druid Lake  avgt   20            NaN                 MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space             JHU to Druid Lake  avgt   20       1177.777 ±       88.971  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space.norm        JHU to Druid Lake  avgt   20   25505064.287 ±   159759.612    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space         JHU to Druid Lake  avgt   20          0.209 ±        0.033  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space.norm    JHU to Druid Lake  avgt   20       4531.967 ±      629.063    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.count                           JHU to Druid Lake  avgt   20         56.950 ±        4.286  counts
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumCommittedAfterGc         JHU to Druid Lake  avgt   20  414066278.400 ±   221004.501   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumUsedAfterGc              JHU to Druid Lake  avgt   20   57014056.000 ± 12354915.856   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.time                            JHU to Druid Lake  avgt   20         71.600 ±        6.537      ms
JmhRuntimeTest.findShortestPath                                        7-11 to Druid Lake  avgt   20          2.140 ±        0.097   ms/op
JmhRuntimeTest.findShortestPath:+c2k.gc.alloc.rate                     7-11 to Druid Lake  avgt   20            NaN                 MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space            7-11 to Druid Lake  avgt   20       1184.914 ±       54.190  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space.norm       7-11 to Druid Lake  avgt   20   25406103.871 ±   166122.045    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space        7-11 to Druid Lake  avgt   20          7.264 ±        5.918  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space.norm   7-11 to Druid Lake  avgt   20     155379.123 ±   126026.135    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.count                          7-11 to Druid Lake  avgt   20         59.250 ±        3.173  counts
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumCommittedAfterGc        7-11 to Druid Lake  avgt   20  408777523.200 ± 11203445.240   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumUsedAfterGc             7-11 to Druid Lake  avgt   20   74257373.600 ±  1070525.148   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.time                           7-11 to Druid Lake  avgt   20        153.400 ±       13.037      ms
JmhRuntimeTest.findShortestPath                                       Inner Harbor to JHU  avgt   20          7.199 ±        0.274   ms/op
JmhRuntimeTest.findShortestPath:+c2k.gc.alloc.rate                    Inner Harbor to JHU  avgt   20            NaN                 MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space           Inner Harbor to JHU  avgt   20       1078.333 ±       75.178  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Eden_Space.norm      Inner Harbor to JHU  avgt   20   26151018.662 ±   183053.432    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space       Inner Harbor to JHU  avgt   20          0.771 ±        0.329  MB/sec
JmhRuntimeTest.findShortestPath:+c2k.gc.churn.G1_Survivor_Space.norm  Inner Harbor to JHU  avgt   20      18271.059 ±     7020.937    B/op
JmhRuntimeTest.findShortestPath:+c2k.gc.count                         Inner Harbor to JHU  avgt   20         76.250 ±        5.070  counts
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumCommittedAfterGc       Inner Harbor to JHU  avgt   20  293545574.400 ±   195639.905   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.maximumUsedAfterGc            Inner Harbor to JHU  avgt   20   69039041.600 ±  1208302.945   bytes
JmhRuntimeTest.findShortestPath:+c2k.gc.time                          Inner Harbor to JHU  avgt   20         92.000 ±       16.425      ms
