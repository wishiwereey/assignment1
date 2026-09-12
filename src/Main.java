import java.awt.*;
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        AlgorithmTest.runAllTests();
        System.out.println("\n--- Small demo ---");
        int[] a = {7, 2, 9, 2, 1, 8, 5, 3};
        int[] mergeArray = a.clone();
        Metrics mergeMetrics = new Metrics();
        MergeSorter.sort(mergeArray, mergeMetrics);
        System.out.println("MergeSort: " + Arrays.toString(mergeArray));
        int[] quickArray = a.clone();
        Metrics quickMetrics = new Metrics();
        QuickSorter.sort(quickArray, quickMetrics);
        System.out.println("QuickSort: " + Arrays.toString(quickArray));
        int[] selectArray = a.clone();
        int k = 3;
        int value = DeterministicSelector.select(selectArray, k, new Metrics());
        System.out.println("Select k=" + k + ": " + value);
        Point[] points = {
                new Point(0, 0),
                new Point(1, 1),
                new Point(5, 5),
                new Point(10, 3)
        };
        double distance = ClosestPairSolver.findClosestPair(points, new Metrics());
        System.out.println("Closest pair distance: " + distance);
        System.out.println("\n--- Running experiments ---");
        Experiment.runExperiments();
    }
}