/* *****************************************************************************
 *  Name:              Yiwei Dai
 *  Coursera User ID:  123456
 *  Last modified:     2/8/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double S_PARA = 1.96; // order

    private final double[] thresholdList;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (trials <= 0 || n <= 0) {
            throw new IllegalArgumentException(
                    "Trials should be larger than 0 and n should bigger than 0");
        }
        thresholdList = new double[trials];
        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            boolean ifPerc = false;
            while (!ifPerc) {
                int[] id1D = StdRandom.permutation(n * n);
                // int[] idy = StdRandom.permutation(n);
                for (int i = 0; i < n * n; i++) {
                    if (!perc.isOpen(id1D[i] / n + 1, id1D[i] % n + 1)) {
                        perc.open(id1D[i] / n + 1, id1D[i] % n + 1);
                    }
                    if (perc.percolates()) {
                        ifPerc = true;
                        break;
                    }
                }
            }

            thresholdList[t] = ((double) perc.numberOfOpenSites()) / (double) (n * n);
            // System.out.println(Arrays.toString(thresholdList));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdList);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdList);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (S_PARA * stddev()) / Math.sqrt(thresholdList.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (S_PARA * stddev()) / Math.sqrt(thresholdList.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percs = new PercolationStats(Integer.parseInt(args[0]),
                                                      Integer.parseInt(args[1]));

        System.out.println("mean\t" + "=" + percs.mean());
        System.out.println("stddev\t" + "=" + percs.stddev());
        System.out.println("95% confidence interval\t" + "= [" + percs.confidenceLo() + "," + percs
                .confidenceHi() + "]");
        // System.out.println(Arrays.toString(StdRandom.permutation(8, 100)));

    }

}
