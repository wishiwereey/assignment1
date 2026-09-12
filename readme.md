### A. Project Overview
The purpose of this assignment was to implement and analyze several divide-and-conquer algorithms and then compare their theoretical complexity with experimental results.
I implemented four algorithms:
- MergeSort
- QuickSort
- Deterministic Select (Median-of-Medians)
- Closest Pair of Points

The program also collects several metrics during execution: running time, maximum recursion depth, number of comparisons, and number of swaps. The experimental results are saved in `results/results.csv`.

### B. Algorithm Analysis
### 1. MergeSort
MergeSort divides the array into two halves, recursively sorts both halves, and then merges them back together.
I used one reusable auxiliary array instead of creating a new array for every merge. For small subarrays, insertion sort is used with a cutoff of 15 elements.
There is also a check before merging. If the last element of the left half is already smaller than or equal to the first element of the right half, the merge can be skipped.

The recurrence is:
```text
T(n) = 2T(n/2) + Θ(n)
```

Using the Master Theorem:

```text
T(n) = Θ(n log n)
```

Time complexity: **Θ(n log n)**  
Extra space: **O(n)**  
Recursion depth: **O(log n)**

### 2. QuickSort

QuickSort chooses a random pivot and partitions the array around it.
My implementation uses 3-way partitioning:

```text
elements < pivot
elements = pivot
elements > pivot
```

This is useful when the array contains many duplicate values.
The algorithm recursively processes the smaller partition and handles the larger partition with a loop. This reduces the recursion stack size.
For a partition of size `k`, the recurrence can be written as:

```text
T(n) = T(k) + T(n-k-1) + Θ(n)
```

With reasonably balanced partitions, the expected complexity is:

```text
O(n log n)
```

The worst case is:

```text
O(n²)
```

The typical recursion depth is **O(log n)** because the smaller partition is processed recursively.


### 3. Deterministic Select
Deterministic Select finds the element at position `k` without sorting the entire array.
The array is divided into groups of five. Each group is sorted and its median is found. The median of these medians is then used as the pivot.
After partitioning, the algorithm continues only in the partition that contains the required index `k`.
I also used 3-way partitioning so duplicate values can be handled efficiently.
The recurrence is approximately:

```text
T(n) <= T(n/5) + T(7n/10) + Θ(n)
```

The two recursive parts are small enough that the total work remains linear:

```text
T(n) = Θ(n)
```

Time complexity: **Θ(n)** in the worst case.

### 4. Closest Pair of Points
The Closest Pair algorithm finds the minimum distance between any two points.
First, the points are sorted by their coordinates. The set is divided into left and right halves, and the closest distance is found recursively in both halves.
After that, a strip is created around the middle line. Only points inside this strip that can improve the current minimum distance need to be checked.

The recurrence is:

```text
T(n) = 2T(n/2) + Θ(n)
```

Using the Master Theorem:

```text
T(n) = Θ(n log n)
```

The brute-force solution takes **Θ(n²)** because it checks every pair of points. The divide-and-conquer version avoids most of these comparisons.

### C. Experimental Results

I tested the algorithms using the following input sizes:

```text
100, 500, 1000, 5000, 10000
```

For sorting, I used:
- Random
- Sorted
- Reverse-sorted
- Duplicate-heavy

For Deterministic Select, I used random, sorted, and duplicate-heavy arrays.

Closest Pair was tested using randomly generated points.

Execution time was measured using `System.nanoTime()`. I also recorded maximum recursion depth, comparisons, and swaps.

### Random Input Results

| Algorithm | n | Time (ns) | Max Depth | Comparisons |
|---|---:|---:|---:|---:|
| MergeSort | 100 | 10,800 | 4 | 663 |
| MergeSort | 1,000 | 340,200 | 8 | 9,566 |
| MergeSort | 5,000 | 1,163,800 | 10 | 59,028 |
| MergeSort | 10,000 | 1,019,200 | 11 | 128,076 |
| QuickSort | 100 | 13,700 | 4 | 689 |
| QuickSort | 1,000 | 129,600 | 6 | 12,480 |
| QuickSort | 5,000 | 581,200 | 8 | 74,427 |
| QuickSort | 10,000 | 1,593,000 | 8 | 163,309 |
| Deterministic Select | 100 | 19,100 | 7 | 632 |
| Deterministic Select | 1,000 | 144,500 | 11 | 7,229 |
| Deterministic Select | 5,000 | 576,000 | 12 | 39,032 |
| Deterministic Select | 10,000 | 608,600 | 13 | 82,312 |
| Closest Pair | 100 | 419,500 | 7 | 642 |
| Closest Pair | 1,000 | 4,575,600 | 10 | 10,068 |
| Closest Pair | 5,000 | 7,082,700 | 12 | 61,446 |
| Closest Pair | 10,000 | 21,022,100 | 13 | 132,885 |

The complete results, including the different input types, are available in `results/results.csv`.

### Effect of Input Type

Input structure had a noticeable effect on performance.
For example, for MergeSort with `n = 10000`:

| Input Type | Time (ns) | Comparisons |
|---|---:|---:|
| Random | 1,019,200 | 128,076 |
| Sorted | 52,100 | 9,999 |
| Reverse | 369,000 | 94,671 |
| Duplicate-heavy | 591,400 | 122,074 |

Sorted input was much faster because the implementation can skip the merge when two halves are already in the correct order.
For QuickSort with `n = 10000`:

| Input Type | Time (ns) | Max Depth |
|---|---:|---:|
| Random | 1,593,000 | 8 |
| Sorted | 877,700 | 8 |
| Reverse | 990,100 | 9 |
| Duplicate-heavy | 271,400 | 2 |

The duplicate-heavy case had a very small recursion depth because 3-way partitioning groups values equal to the pivot together.

### Execution Time vs. n

![Execution Time](docs/plots/time_vs_n.png)

In general, execution time increased with input size. The values are not perfectly smooth because Java execution time can be affected by JVM warm-up, garbage collection, and other processes running on the computer.
Closest Pair had the highest measured execution time in these experiments.

### Recursion Depth vs. n

![Recursion Depth](docs/plots/depth_vs_n.png)

The recursion depth increased much more slowly than the input size.
For random input at `n = 10000`:

| Algorithm | Max Recursion Depth |
|---|---:|
| MergeSort | 11 |
| QuickSort | 8 |
| Deterministic Select | 13 |
| Closest Pair | 13 |

This is consistent with the divide-and-conquer structure of the algorithms.

### D. Discussion

### Do the results match theoretical complexity?

Mostly yes. The experimental values are not perfectly smooth, but the overall growth follows the expected behavior.
MergeSort and QuickSort show approximately `n log n` behavior. Deterministic Select grows closer to linear because it only continues into the required partition. Closest Pair also follows the expected divide-and-conquer growth, although its actual execution time is higher than the other algorithms.
The number of comparisons is sometimes more useful than execution time for seeing this pattern because execution time can be affected by the JVM and the computer.

### How does input structure affect performance?

The input structure affected the algorithms differently.
MergeSort was especially fast on sorted input because my implementation checks whether a merge is necessary.
QuickSort uses a random pivot, so sorted or reverse-sorted input does not automatically create the worst case.
Duplicate-heavy arrays worked well with QuickSort because of 3-way partitioning.

### Why does smaller-first recursion help QuickSort?

If QuickSort recursively calls itself on both partitions, very unbalanced partitions can create a large recursion stack.
In my implementation, only the smaller partition is handled recursively. The larger one is processed using a loop.
This keeps the stack depth small. In my random test with `n = 10000`, the maximum QuickSort recursion depth was 8.

### Why does Median-of-Medians guarantee O(n)?

Median-of-Medians chooses the pivot using groups of five.
This prevents the pivot from being extremely bad every time. A guaranteed part of the array can be removed after each partition.
The recurrence is:

```text
T(n) <= T(n/5) + T(7n/10) + Θ(n)
```

which gives linear worst-case complexity:

```text
Θ(n)
```

### Why is divide-and-conquer Closest Pair faster than O(n²) for large inputs?

The brute-force algorithm compares every pair of points.
The divide-and-conquer algorithm first solves two smaller problems and then only checks points close to the dividing line.
Because many unnecessary pairs are ignored, its complexity is reduced from:

```text
Θ(n²)
```

to:

```text
Θ(n log n)
```

### What practical factors affect performance?

Theoretical complexity is not the only thing that affects execution time.
Some practical factors are:

- JVM warm-up
- JIT compilation
- garbage collection
- CPU cache
- memory allocation
- other programs running at the same time

This is why some measured times can go down even when the input size increases.
For example, MergeSort on random input took `1,163,800 ns` for `n = 5000` and `1,019,200 ns` for `n = 10000` in this experiment. This does not mean that the larger input has better asymptotic complexity. It is normal variation in experimental timing.

---

### E. Reflection

This assignment helped me understand divide-and-conquer algorithms better because I had to implement them instead of only working with their formulas. One thing I noticed was that small implementation choices can make a visible difference. For example, skipping an unnecessary merge helped MergeSort on sorted arrays, and 3-way partitioning helped a lot when QuickSort had many duplicate values.
The most difficult parts for me were Median-of-Medians and Closest Pair. Median-of-Medians was harder to understand at first because choosing the pivot requires several extra steps. For Closest Pair, the strip part was the most confusing part because it is important to avoid comparing every pair again. The experiments also showed me that theoretical complexity and real execution time are related, but real measurements are less predictable because of the JVM and the system.

### F. Screenshots

### Program Output

![Program Output](docs/screenshots/execution.png)

### Test Results

![Test Results](docs/screenshots/test_results.png)

### Experimental Results

![Experimental Results](docs/screenshots/results_csv.png)

### Plots

![Execution Time Plot](docs/plots/time_vs_n.png)

![Recursion Depth Plot](docs/plots/depth_vs_n.png)