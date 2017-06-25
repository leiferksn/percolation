import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by leiferksn on 19.06.17.
 */
public class PercolationStats {

    private double[] thresholds = null;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Number of trials and/or number of sites must be positive.");
        }
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int random = StdRandom.uniform(1, n * n);
                int row = 0;
                if (random % n  == 0) {
                       row = (int) Math.floor((double) random / n);
                } else {
                    row = (int) Math.floor((double) random / n) + 1;
                }
                int col = (random + n) - row * n;
                p.open(row, col);
            }
            thresholds[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(thresholds.length));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(thresholds.length));
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean = " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
