public class MergeSorter {
    private static final int CUTOFF = 15;
    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length < 2) return;
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1, metrics);
    }
    private static void sort(int[] a, int[] aux, int low, int high, Metrics metrics) {
        metrics.enterRecursion();
        if (high - low + 1 <= CUTOFF) {
            insertionSort(a, low, high, metrics);
            metrics.exitRecursion();
            return;
        }
        int mid = low + (high - low) / 2;
        sort(a, aux, low, mid, metrics);
        sort(a, aux, mid + 1, high, metrics);
        metrics.addComparison();
        if (a[mid] > a[mid + 1])
            merge(a, aux, low, mid, high, metrics);
        metrics.exitRecursion();
    }
    private static void merge(int[] a, int[] aux,
                              int low, int mid, int high,
                              Metrics metrics) {
        for (int i = low; i <= high; i++)
            aux[i] = a[i];
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > high)
                a[k] = aux[i++];
            else {
                metrics.addComparison();
                if (aux[i] <= aux[j])
                    a[k] = aux[i++];
                else
                    a[k] = aux[j++];
            }
            metrics.addSwap();
        }
    }
    private static void insertionSort(int[] a, int low, int high, Metrics metrics) {
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
}