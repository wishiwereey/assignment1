### A.Overview
The purpose of this assignment was to implement and analyze several divide-and-conquer algorithms and then compare their theoretical complexity with experimental results.
I implemented four algorithms:
- MergeSort
- QuickSort
- Deterministic Select (Median-of-Medians)
- Closest Pair of Points

The program also collects several metrics during execution: running time, maximum recursion depth, number of comparisons, and number of swaps. The experimental results are saved in results/results.csv.

### B.Algorithm Analysis
### MergeSort
MergeSort divides the array into two halves, sorts them recursively, and merges them back together. I used one reusable auxiliary array and insertion sort for small subarrays with a cutoff of 15. The merge is skipped if the two halves are already ordered.
- Recurrence: T(n) = 2T(n/2) + Θ(n)
- Time: Θ(n log n)
- Extra space: O(n)
- Recursion depth: O(log n)
- 
### QuickSort
QuickSort chooses a random pivot and uses 3-way partitioning to separate elements into "< pivot", "= pivot", and "> pivot". This works well with duplicate values. The smaller partition is processed recursively, while the larger one is handled with a loop to reduce recursion depth.
- Recurrence: T(n) = T(k) + T(n-k-1) + Θ(n)
- Average time: O(n log n)
- Worst case: O(n²)
- Typical recursion depth: O(log n)

### Deterministic Select
Deterministic Select finds the element at position k without sorting the whole array. It divides the array into groups of five and uses the median of medians as the pivot. After partitioning, it continues only in the part containing k. 3-way partitioning is also used to handle duplicates.
- Recurrence: T(n) ≤ T(n/5) + T(7n/10) + Θ(n)
- Worst-case time: Θ(n)

### Closest Pair of Points
The Closest Pair algorithm finds the minimum distance between two points. It divides the points into two halves, solves both recursively, and then checks only the necessary points in a strip around the middle line.
- Recurrence: T(n) = 2T(n/2) + Θ(n)
- Time: Θ(n log n)
- Brute-force time: Θ(n²)

### Experimental Results
I tested all algorithms with input sizes from 100 to 10000. MergeSort and QuickSort were tested on random, sorted, reverse-sorted, and duplicate-heavy arrays. Deterministic Select used random, sorted, and duplicate-heavy inputs, while Closest Pair used random points.
I measured execution time with System.nanoTime(), recursion depth, comparisons, and swaps. The median execution times were saved to results/results.csv.

### Random Input Results
| Algorithm | n | Time (ns) | Max Depth | Comparisons |
|---|---:|---:|---:|---:|
| MergeSort | 100 | 74,500 | 4 | 663 |
| MergeSort | 1,000 | 419,500 | 8 | 9,566 |
| MergeSort | 5,000 | 906,300 | 10 | 59,028 |
| MergeSort | 10,000 | 1,062,600 | 11 | 128,076 |
| QuickSort | 100 | 114,700 | 5 | 625 |
| QuickSort | 1,000 | 99,600 | 7 | 11,226 |
| QuickSort | 5,000 | 723,700 | 8 | 68,713 |
| QuickSort | 10,000 | 1,033,500 | 9 | 164,575 |
| Deterministic Select | 100 | 89,200 | 7 | 632 |
| Deterministic Select | 1,000 | 348,000 | 11 | 7,229 |
| Deterministic Select | 5,000 | 293,200 | 12 | 39,032 |
| Deterministic Select | 10,000 | 430,800 | 13 | 82,312 |
| Closest Pair | 100 | 451,400 | 7 | 642 |
| Closest Pair | 1,000 | 1,868,900 | 10 | 10,068 |
| Closest Pair | 5,000 | 11,804,300 | 12 | 61,446 |
| Closest Pair | 10,000 | 18,407,900 | 13 | 132,885 |

The complete results for all input types and sizes are available in results/results.csv.

### Effect of Input Type
The input structure had a noticeable effect on the results.
For MergeSort with n = 10000:

| Input Type | Time (ns) | Comparisons |
|---|---:|---:|
| Random | 1,062,600 | 128,076 |
| Sorted | 33,900 | 9,999 |
| Reverse | 271,400 | 94,671 |
| Duplicate-heavy | 468,700 | 122,074 |

Sorted input was much faster because my MergeSort implementation checks whether the two halves are already in the correct order before merging them. If they are already ordered, the merge step is skipped.
For QuickSort with n = 10000:

| Input Type | Time (ns) | Max Depth |
|---|---:|---:|
| Random | 1,033,500 | 9 |
| Sorted | 1,028,100 | 9 |
| Reverse | 1,012,500 | 8 |
| Duplicate-heavy | 272,700 | 2 |

Random, sorted, and reverse-sorted inputs produced fairly similar execution times at this size. Duplicate-heavy input was faster and had a recursion depth of only 2 because the 3-way partition groups elements equal to the pivot together.
For Deterministic Select with n = 10000:

| Input Type | Time (ns) | Comparisons |
|---|---:|---:|
| Random | 430,800 | 82,312 |
| Sorted | 140,400 | 58,833 |
| Duplicate-heavy | 166,100 | 30,842 |

The duplicate-heavy input required much fewer comparisons than random input because equal values can be handled together during 3-way partitioning.

### Execution Time vs. n
![Execution Time](docs/plots/time_vs_n.png)

The execution-time plot shows that running time generally increases with input size. MergeSort and QuickSort had similar results on large random inputs, while Deterministic Select was faster because it only searches for one element.
Closest Pair had the highest execution time because it works with points and temporary arrays. The timings are not always smooth because Java performance can be affected by the JVM, JIT compilation, garbage collection, and other system processes.

### Recursion Depth vs. n
![Recursion Depth](docs/plots/depth_vs_n.png)

For random input, the maximum recursion depth was:

| n | MergeSort | QuickSort | Deterministic Select | Closest Pair |
|---:|---:|---:|---:|---:|
| 100 | 4 | 5 | 7 | 7 |
| 500 | 7 | 5 | 8 | 9 |
| 1,000 | 8 | 7 | 11 | 10 |
| 5,000 | 10 | 8 | 12 | 12 |
| 10,000 | 11 | 9 | 13 | 13 |

The recursion depth grows much more slowly than the input size.
MergeSort and Closest Pair repeatedly divide their input into smaller parts. QuickSort also keeps a relatively small recursion depth because only the smaller partition is processed recursively.

### D.Discussion
### Do the results match theoretical complexity?
In general, yes. MergeSort, QuickSort, and Closest Pair show behavior close to n log n, while Deterministic Select is closer to linear. The timings are not perfectly smooth because actual performance also depends on implementation and JVM behavior.

### How does input structure affect performance?
Input structure affected performance. MergeSort was much faster on sorted input because it can skip unnecessary merges. QuickSort handled sorted and reverse inputs well because it uses a randomized pivot. Duplicate-heavy input was especially fast for QuickSort because 3-way partitioning groups equal values together.

### Why does smaller-first recursion help QuickSort?
Processing the smaller partition recursively keeps the call stack small. The larger partition is handled with a loop, which helps maintain about O(log n) recursion depth.

### Why does Median-of-Medians guarantee O(n)?
Median-of-Medians chooses a reliable pivot using groups of five. This guarantees that a fixed part of the input is removed each time, giving a worst-case time complexity of Θ(n).

### Why is divide-and-conquer Closest Pair faster than O(n²) for large inputs?
Brute force checks every pair, giving Θ(n²). Divide-and-conquer splits the points and only checks necessary pairs near the dividing line, reducing the complexity to Θ(n log n).

### What practical factors affect performance?
Actual execution time can vary because of JVM warm-up, JIT compilation, garbage collection, CPU cache, and other running programs. Because of this, measured times do not always increase smoothly with input size.

### E.Reflection
The most difficult parts were managing recursion depth and implementing QuickSort and Closest Pair correctly. QuickSort required smaller-first recursion, while Closest Pair required keeping the points correctly ordered.
The experiments helped me to understand that input structure can affect execution time, especially for sorted and duplicate-heavy data.

### F.Screenshots
### Program Output and 
![Program Output](docs/screenshots/execution.png)

### Test Results
![Test Results](docs/screenshots/test_results.png)
### Experimental Results
![Experimental Results](docs/screenshots/results_csv.png)

### Project Structure
![Project Structure](docs/screenshots/project_structure.png)

### Plots
![Execution Time Plot](docs/plots/time_vs_n.png)

![Recursion Depth Plot](docs/plots/depth_vs_n.png)