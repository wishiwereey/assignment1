import java.util.Random;
public class QuickSorter {
    private static final Random random = new Random();
    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length < 2) return;
        sort(a, 0, a.length - 1, metrics);
    }
    private static void sort(int[] a, int low, int high, Metrics metrics) {
        while (low < high) {
            metrics.enterRecursion();
            int pivot = a[low + random.nextInt(high - low + 1)];
            int[] p = partition(a, low, high, pivot, metrics);
            if (p[0] - low < high - p[1]) {
                sort(a, low, p[0] - 1, metrics);
                low = p[1] + 1;
            } else {
                sort(a, p[1] + 1, high, metrics);
                high = p[0] - 1;
            }
            metrics.exitRecursion();
        }
    }
    private static int[] partition(int[] a, int low, int high, int pivot, Metrics metrics) {
        int left = low;
        int i = low;
        int right = high;
        while (i <= right) {
            metrics.addComparison();
            if (a[i] < pivot) {
                swap(a, left++, i++, metrics);
            } else if (a[i] > pivot) {
                swap(a, i, right--, metrics);
            } else {
                i++;
            }
        }
        return new int[]{left, right};
    }
    private static void swap(int[] a, int i, int j, Metrics metrics) {
        if (i == j) return;
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        metrics.addSwap();
    }
}