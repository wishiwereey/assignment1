import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
public class Experiment {
    private static final int[] SIZES = {100, 500, 1000, 5000, 10000};
    private static final int RUNS = 5;
    private static final Random random = new Random(12345);
    public static void runExperiments() {
        new File("results").mkdirs();
        try (PrintWriter out = new PrintWriter(
                new FileWriter("results/results.csv"))) {
            out.println(
                    "Algorithm,InputType,Size," +
                            "ExecutionTimeNs,MaxRecursionDepth," +
                            "Comparisons,Swaps"
            );
            for (int n : SIZES) {
                runSort(out, "MergeSort", "Random", n);
                runSort(out, "MergeSort", "Sorted", n);
                runSort(out, "MergeSort", "Reverse", n);
                runSort(out, "MergeSort", "DuplicateHeavy", n);
                runSort(out, "QuickSort", "Random", n);
                runSort(out, "QuickSort", "Sorted", n);
                runSort(out, "QuickSort", "Reverse", n);
                runSort(out, "QuickSort", "DuplicateHeavy", n);
                runSelect(out, "Random", n);
                runSelect(out, "Sorted", n);
                runSelect(out, "DuplicateHeavy", n);
                runClosestPair(out, n);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Experiments finished!");
        System.out.println("Results saved to results/results.csv");

    }
    private static void runSort(PrintWriter out,
                                String algorithm,
                                String type,
                                int n) {
        long[] times = new long[RUNS];
        Metrics last = null;
        for (int r = 0; r < RUNS; r++) {
            int[] a = makeArray(n, type);
            Metrics metrics = new Metrics();
            long start = System.nanoTime();
            if (algorithm.equals("MergeSort"))
                MergeSorter.sort(a, metrics);
            else
                QuickSorter.sort(a, metrics);
            times[r] = System.nanoTime() - start;
            last = metrics;
            if (!sorted(a))
                throw new RuntimeException("Sort failed");
        }
        out.printf(
                "%s,%s,%d,%d,%d,%d,%d%n",
                algorithm,
                type,
                n,
                median(times),
                last.maxRecursionDepth,
                last.comparisons,
                last.swaps
        );
    }
    private static void runSelect(PrintWriter out, String type, int n) {
        long[] times = new long[RUNS];
        Metrics last = null;
        for (int r = 0; r < RUNS; r++) {
            int[] a = makeArray(n, type);
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            int k = n / 2;
            Metrics metrics = new Metrics();
            long start = System.nanoTime();
            int result =
                    DeterministicSelector.select(
                            a, k, metrics);
            times[r] = System.nanoTime() - start;
            last = metrics;
            if (result != sorted[k])
                throw new RuntimeException("Select failed");
        }
        out.printf(
                "DeterministicSelect,%s,%d,%d,%d,%d,%d%n",
                type,
                n,
                median(times),
                last.maxRecursionDepth,
                last.comparisons,
                last.swaps
        );
    }
    private static void runClosestPair(PrintWriter out, int n) {
        long[] times = new long[RUNS];
        Metrics last = null;
        for (int r = 0; r < RUNS; r++) {
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(
                        random.nextDouble() * 1000,
                        random.nextDouble() * 1000
                );
            }
            Metrics metrics = new Metrics();
            long start = System.nanoTime();
            ClosestPairSolver.findClosestPair(
                    points, metrics);
            times[r] = System.nanoTime() - start;
            last = metrics;
        }
        out.printf(
                "ClosestPair,Random,%d,%d,%d,%d,%d%n",
                n,
                median(times),
                last.maxRecursionDepth,
                last.comparisons,
                0
        );
    }
    private static int[] makeArray(int n, String type) {
        int[] a = new int[n];
        if (type.equals("Sorted")) {
            for (int i = 0; i < n; i++)
                a[i] = i;
        } else if (type.equals("Reverse")) {
            for (int i = 0; i < n; i++)
                a[i] = n - i;
        } else if (type.equals("DuplicateHeavy")) {
            for (int i = 0; i < n; i++)
                a[i] = random.nextInt(10);
        } else {
            for (int i = 0; i < n; i++)
                a[i] = random.nextInt(n * 10);
        }
        return a;
    }
    private static boolean sorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i - 1] > a[i])
                return false;
        return true;
    }
    private static long median(long[] a) {
        Arrays.sort(a);
        return a[a.length / 2];
    }
}