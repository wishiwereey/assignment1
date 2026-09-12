import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
public class ClosestPairSolver {
    public static double findClosestPair(Point[] points, Metrics metrics) {
        if (points == null || points.length < 2)
            return 0;
        Point[] px = points.clone();
        Point[] py = points.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        return closest(px, py, metrics);
    }
    private static double closest(Point[] px, Point[] py, Metrics metrics) {
        metrics.enterRecursion();
        int n = px.length;
        if (n <= 3) {
            double result = bruteForce(px, metrics);
            metrics.exitRecursion();
            return result;
        }
        int mid = n / 2;
        Point[] leftX = Arrays.copyOfRange(px, 0, mid);
        Point[] rightX = Arrays.copyOfRange(px, mid, n);
        IdentityHashMap<Point, Boolean> left = new IdentityHashMap<>();
        for (Point p : leftX)
            left.put(p, true);
        Point[] leftY = new Point[mid];
        Point[] rightY = new Point[n - mid];
        int l = 0, r = 0;
        for (Point p : py) {
            if (left.containsKey(p))
                leftY[l++] = p;
            else
                rightY[r++] = p;
        }
        double d1 = closest(leftX, leftY, metrics);
        double d2 = closest(rightX, rightY, metrics);
        double d = Math.min(d1, d2);
        Point[] strip = new Point[n];
        int size = 0;
        double middleX = px[mid].x;
        for (Point p : py) {
            metrics.addComparison();
            if (Math.abs(p.x - middleX) < d)
                strip[size++] = p;
        }
        double result = Math.min(d, stripClosest(strip, size, d, metrics));
        metrics.exitRecursion();
        return result;
    }
    private static double stripClosest(Point[] strip, int n, double d, Metrics metrics) {
        double min = d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (strip[j].y - strip[i].y >= min)
                    break;
                metrics.addComparison();
                double dist = strip[i].distanceTo(strip[j]);
                if (dist < min)
                    min = dist;
            }
        }
        return min;
    }
    private static double bruteForce(Point[] points, Metrics metrics) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                metrics.addComparison();
                double d = points[i].distanceTo(points[j]);
                if (d < min)
                    min = d;
            }
        }
        return min;
    }
}