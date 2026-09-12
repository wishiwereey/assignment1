public class Metrics {
    public long comparisons = 0;
    public long swaps = 0;
    public int currentRecursionDepth = 0;
    public int maxRecursionDepth = 0;
    public void addComparison() {
        comparisons++;
    }
    public void addSwap() {
        swaps++;
    }
    public void enterRecursion() {
        currentRecursionDepth++;
        if (currentRecursionDepth > maxRecursionDepth)
            maxRecursionDepth = currentRecursionDepth;
    }
    public void exitRecursion() {
        currentRecursionDepth--;
    }
}