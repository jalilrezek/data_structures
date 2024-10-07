# Discussion
Abbreviations:
OAHM = OpenAddressingHashMap
CHM = ChainingHashMap

I show my data below. I ran, for each of my HashMaps, four scenarios, for a total of eight runs.
**Load factor 0.67, remove checks has(); 
**load factor 0.67, remove doesn't check has(); 
**remove checks has, load factor = 0.75; and 
**remove doesn't check has(), load factor = 0.75

The JdkHashMap outperformed my two HashMaps in both spatial and temporal metrics; this is not unexpected since 
I assume that Java's built in HashMap may incorporate some optimization techniques that we haven't learned about
yet. 

Between my two HashMaps, it was hard to decide which was better.

One optimization I tried was to prevent remove() from checking has() at the start of the function. If remove()
never finds the element, it could simply throw the IllegalArgumentException at the very end, since that's 
equivalent to has() returning false. As expected, this led to some improvement for the CHM, but counterintuitively
it appears to have worsened performance for the OAHM.

I tried to implement quadratic probing for the OAHM, but this was more difficult to implement and did not 
result in any appreciable changes in performance. Therefore, I stuck with linear probing for the OAHM. For the
CHM, I used ArrayLists as the auxiliary data structures.

To rehash, I check if the ratio of the number of occupied spots (which, for the OAHM, includes tombstones) 
to the length of the underlying array >= the load factor, and if so, I reassign all the entries to a new array.
The size of the new array is the next prime number at least twice as large as the previous size; once I run out
of primes, I switch to doubling the size from there on out.

**On the load factors**

For both maps, the results were different to interpret. For the OAHM, with a load factor of 0.67, the 
ms/op scores for the OAHM with remove checking has() were higher for some files than the OAHM with remove
not checking has(), but lower for others. Likewise for the build times. The same trend appears for the two
OAHMs with load factor of 0.75, and for all the CHM implementations, except for the case below:

For the CHM, a load factor of 0.75 appears to be better based on the runtimes for apache.txt, newegg.txt, and 
random164.txt. For the CHM, the load factor of 0.75 also appears to have reduced the space metrics for these files.

So, for the CHM, it seems a load factor of 0.75 is better based on both the ms/op scores and the space metrics.
For the OAHM, the data is ambiguous whether a load factor of 0.75 or 0.67 is better for temporal and spatial
performance metrics.

**On whether the remove() function should check has() at first or not based on my results**

For the CHM with load factor 0.75, ms/op scores were lower for the version with a remove() function that 
doesn't check has. But with load factor 0.67, the data was ambiguous (some files had one version better,
other files had the other version better). The spatial complexity metrics were ambiguous on this matter.

For the OAHM, based on ms/op scores, with load factor of 0.67, with contradictory results from different files,
it was hard to tell whether the version with remove checking has() outperformed the version with 
remove not checking has(). But, for load factor 0.75, it appears that the version with remove checking has() overall
outperformed the version with remove not checking has(); this was unexpected. The spatial usage metrics
were ambiguous on this matter.

For instance, regarding the OAHM, on the file apache.txt, the OAHM with load factor 0.75 whose remove() function
checked has() had a score of 126.862 ms/op, and the OAHM with load factor 0.75 whose remove() doesn't check
has() had a score of 143.444 ms/op.

Sometimes, the CHM required almost 10x as much space as the corresponding OAHM. At other times, their space
usage metrics were comparable. It is surprising that they would ever be comparable, since the CHM has to store
the additional data structures in each index of the underlying array (the "buckets" which for me were 
ArrayLists).
For instance, my ChainingHashMap with load factor 0.67 and with remove checking the has() function, on the file
jhu.txt, had 187334656.000 bytes used. The OpenAddressingHashMap of the same kind used 27718952.000 bytes, nearly
10x fewer. 

**Final recommendation of which HashMap to use**

Therefore, based on space considerations and the results of my data, I recommend a load factor of 0.75 for the
CHM, and would do so for the OAHM too just to be safe, although the data is ambiguous for the OAHM. I would 
recommend using the implementation of CHM where the remove() function doesn't check the has() function, but
for the OAHM I would recommend the implementation where remove() does check the has() function, based on the
fact that it appeared to perform better on ms/op metrics, even though this result is unexpected. 

In general, I would recommend using the OAHM because it requires fewer bytes to operate, in particular the OAHM
with load factor 0.75 and whose remove() function checks the has() function.



Condensed data about runtime and memory usage:

**JdkHashMap**

Benchmark
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         129.777           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.149           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.055           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          64.607           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         454.758           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.017           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   121346088.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27800828.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28084032.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    71800340.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   609640776.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    26907252.000           bytes


**OpenAddressingHashMap, remove() checks has(), load factor = 0.67** -- seems to do better than 0.67 not checking has()
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         138.787           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.161           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.057           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          72.796           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         511.042           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.016           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   120074104.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27718952.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28560392.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    67519172.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   605637984.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    27093432.000           bytes


**OpenAddressingHashMap, remove function does not check has(), load factor = 0.67**
Time to complete: 6:31
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         144.350           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.170           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.059           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          80.642           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         487.310           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.017           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   116144816.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27845472.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28030504.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    70912376.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   645486416.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    27033748.000           bytes


**OpenAddressingHashMap, remove() checks has(), load factor = 0.75**
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         126.862           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.163           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.058           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          94.853           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         538.937           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.017           ms/op


Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   122608312.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27760116.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28082364.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    68872952.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   644327624.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    27035492.000           bytes


**OpenAddressingHashMap, remove function does not check has(), load factor = 0.75**
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         143.444           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.167           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.056           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          78.889           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         574.612           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.018           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   117805768.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27837956.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28040096.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    70671956.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   719426008.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    26824740.000           bytes


**ChainingHashMap, remove() checks has(), load factor = 0.67**
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         152.883           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.168           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.068           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          75.484           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         783.815           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.017           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   174597168.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumCommittedAfterGc                            jhu.txt  avgt    2   187334656.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28350356.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    94494684.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   719421024.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    26934236.000           bytes



**ChainingHashMap, remove() doesn't check has, load factor = 0.67**

Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         178.266           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.182           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.074           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2         122.853           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         781.832           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.020           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   180357092.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27891448.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28208952.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    99151360.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   625890880.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    26694368.000           bytes


**ChainingHashMap, remove function checks has(), load factor = 0.75**
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         168.872           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.176           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.074           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          81.921           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         679.050           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.017           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   150138200.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27650452.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28266888.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    92544528.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   686415800.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    27043732.000           bytes

**ChainingHashMap, remove doesn't check has(), load factor = 0.75**
Benchmark                                                                                (fileName)  Mode  Cnt           Score   Error   Units
Time:
JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         149.146           ms/op
JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.177           ms/op
JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.073           ms/op
JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          79.455           ms/op
JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         640.169           ms/op
JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.018           ms/op

Space:
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2   149261888.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    27629088.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    28414876.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    91316508.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2   634365576.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    26922672.000           bytes


