/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/09
 *  Description: Algorithm Assignment, Percolation threshold estimation.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double Z_SCORE_OF_95_CONFIDENCE = 1.96;
    private final double meanRecord;    // Save Mean once calculated.
    private final double stdDevRecord;  // Save StdDev once calculated.
    private final double marginOfError; // Confidence interval radius.

    /*
     * perform trials independent experiments on an n-by-n grid
     */
    public PercolationStats(int n, int trials) {
        if (n < 1)
            throw new IllegalArgumentException("Invalid Model size");

        if (trials < 1)
            throw new IllegalArgumentException("Invalid Trial Count");

        int[] numOfOpenSites = new int[trials];
        int row, col;
        Percolation model;
        double totalSites = n * n;

        do {
            model = new Percolation(n);
            do {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                model.open(row, col);
            } while (!model.percolates());
            numOfOpenSites[--trials] = model.numberOfOpenSites();
        } while (trials > 0);

        meanRecord = StdStats.mean(numOfOpenSites) / totalSites;
        stdDevRecord = StdStats.stddev(numOfOpenSites) / totalSites;
        marginOfError = Z_SCORE_OF_95_CONFIDENCE * stddev() / Math.sqrt(numOfOpenSites.length);
    }

    public double stddev() // sample standard deviation of percolation threshold
    {
        return stdDevRecord;
    }

    public static void main(String[] args) // test client (described below)
    {
        if (args.length < 2)
            throw new IllegalArgumentException("Needs two Arguments for model size and trial");

        int modelSize = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);

        PercolationStats modelStats = new PercolationStats(modelSize, trial);
        StdOut.println("Mean = " + modelStats.mean());
        StdOut.println("StdDev = " + modelStats.stddev());
        StdOut.println("95% confidence interval = [" + modelStats.confidenceLo() + ", " + modelStats
                .confidenceHi() + "]");
    }

    public double mean() // sample mean of percolation threshold
    {
        return meanRecord;
    }

    public double confidenceLo() // low  endpoint of 95% confidence interval
    {
        return meanRecord - marginOfError;
    }

    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        return meanRecord + marginOfError;
    }
}
