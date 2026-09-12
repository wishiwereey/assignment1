public class DeterministicSelector {

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0 || k < 0 || k >= a.length)
            throw new IllegalArgumentException();

        return select(a, 0, a.length - 1, k, metrics);
    }

    private static int select(int[] a, int low, int high,
                              int k, Metrics metrics) {
        metrics.enterRecursion();

        if (low == high) {
            int result = a[low];
            metrics.exitRecursion();
            return result;
        }

        int pivot = medianOfMedians(a, low, high, metrics);
        int[] p = partition(a, low, high, pivot, metrics);

        int result;

        if (k < p[0])
            result = select(a, low, p[0] - 1, k, metrics);
        else if (k > p[1])
            result = select(a, p[1] + 1, high, k, metrics);
        else
            result = pivot;

        metrics.exitRecursion();
        return result;
    }

    private static int medianOfMedians(int[] a, int low, int high,
                                       Metrics metrics) {
        int n = high - low + 1;

        if (n <= 5) {
            insertionSort(a, low, high, metrics);
            return a[low + n / 2];
        }

        int pos = low;

        for (int i = low; i <= high; i += 5) {
            int end = Math.min(i + 4, high);

            insertionSort(a, i, end, metrics);

            int median = i + (end - i) / 2;
            swap(a, pos++, median, metrics);
        }

        return select(a, low, pos - 1,
                low + (pos - low) / 2, metrics);
    }

    private static int[] partition(int[] a, int low, int high,
                                   int pivot, Metrics metrics) {
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

    private static void insertionSort(int[] a, int low, int high,
                                      Metrics metrics) {
        for (int i = low + 1; i <= high; i++) {
            int x = a[i];
            int j = i - 1;

            while (j >= low) {
                metrics.addComparison();

                if (a[j] <= x) break;

                a[j + 1] = a[j];
                metrics.addSwap();
                j--;
            }

            a[j + 1] = x;
        }
    }

    private static void swap(int[] a, int i, int j, Metrics metrics) {
        if (i == j) return;

        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        metrics.addSwap();
    }
}