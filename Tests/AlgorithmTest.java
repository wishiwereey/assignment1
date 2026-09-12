import java.util.Arrays;
import java.util.Random;
public class AlgorithmTest {
    private static final Random random = new Random(12345);
    public static void runAllTests() {
        testSorting();
        testSelect();
        testClosestPair();
        System.out.println("All tests passed!");
    }
    private static void testSorting() {
        for (int t = 0; t < 100; t++) {
            int[] a = randomArray(100);
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] merge = a.clone();
            MergeSorter.sort(merge, new Metrics());
            if (!Arrays.equals(merge, expected))
                throw new RuntimeException("MergeSort failed");
            int[] quick = a.clone();
            QuickSorter.sort(quick, new Metrics());
            if (!Arrays.equals(quick, expected))
                throw new RuntimeException("QuickSort failed");
        }
        int[][] cases = {
                {},
                {5},
                {1, 2, 3, 4, 5},
                {5, 4, 3, 2, 1},
                {2, 2, 2, 2, 2}
        };
        for (int[] a : cases) {
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] m = a.clone();
            int[] q = a.clone();
            MergeSorter.sort(m, new Metrics());
            QuickSorter.sort(q, new Metrics());
            if (!Arrays.equals(m, expected))
                throw new RuntimeException("MergeSort edge case failed");
            if (!Arrays.equals(q, expected))
                throw new RuntimeException("QuickSort edge case failed");
        }
    }
    private static void testSelect() {
        for (int t = 0; t < 100; t++) {
            int[] a = randomArray(100);
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            int k = random.nextInt(a.length);
            int result = DeterministicSelector.select(a, k, new Metrics());
            if (result != sorted[k])
                throw new RuntimeException("Select failed");
        }
        int[] a = {5, 2, 5, 1, 5, 2, 5, 3};
        int[] sorted = a.clone();
        Arrays.sort(sorted);
        for (int k = 0; k < a.length; k++) {
            int result = DeterministicSelector.select(a.clone(), k, new Metrics());
            if (result != sorted[k])
                throw new RuntimeException("Select duplicate test failed");
        }
    }
    private static void testClosestPair() {
        int[] sizes = {10, 50, 100, 500, 1000, 2000};
        for (int n : sizes) {
            Point[] points = randomPoints(n);
            double expected = bruteForce(points);
            double result = ClosestPairSolver.findClosestPair(points, new Metrics());
            if (Math.abs(result - expected) > 1e-9)
                throw new RuntimeException("ClosestPair failed for n=" + n);
        }
        Point[] sameX = {
                new Point(5, 1),
                new Point(5, 2),
                new Point(5, 10),
                new Point(20, 20)
        };
        double expected = bruteForce(sameX);
        double result = ClosestPairSolver.findClosestPair(sameX, new Metrics());
        if (Math.abs(result - expected) > 1e-9)
            throw new RuntimeException("ClosestPair same-x test failed");
    }
    private static double bruteForce(Point[] points) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double d = points[i].distanceTo(points[j]);
                if (d < min)
                    min = d;
            }
        }
        return min;
    }
    private static int[] randomArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = random.nextInt(1000);
        return a;
    }
    private static Point[] randomPoints(int n) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
        return points;
    }
}
