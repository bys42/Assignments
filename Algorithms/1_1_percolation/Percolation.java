/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/09
 *  Description: Algorithm Assignment, Percolation model.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int topIndex;                // Top's index in UnionFind.
    private final int bottomIndex;             // Bottom's index in UnionFind.
    private final WeightedQuickUnionUF siteUf; // UnionFind: n*n Sites, Top & Bottom.
    private final int modelSize;               // Row & Column size of Model.
    private final WeightedQuickUnionUF siteUfForFull; // UnionFind: n*n Sites, Top.
    private boolean[][] isOpenSite;      // Site's Open Status Record.
    private int openSitesCount;          // Number of openned sites.

    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n < 1) throw new IllegalArgumentException("Invalid model size");

        isOpenSite = new boolean[n][n];
        topIndex = n * n;
        bottomIndex = topIndex + 1;
        siteUf = new WeightedQuickUnionUF(bottomIndex + 1);
        modelSize = n;

        siteUfForFull = new WeightedQuickUnionUF(topIndex + 1);
    }

    /*
     * Starts a percolation test on a n*n model,
     * where n = args[0] (or a random integer from 1 to 10 if not exist)
     */
    public static void main(String[] args) {
        int testSize, row, col;
        Percolation model;

        if (args.length > 0)
            testSize = Integer.parseInt(args[0]);
        else
            testSize = StdRandom.uniform(1, 11);

        model = new Percolation(testSize);
        StdOut.println("Test on " + testSize + "x" + testSize + " model");

        do {
            do {
                row = StdRandom.uniform(testSize) + 1;
                col = StdRandom.uniform(testSize) + 1;
            } while (model.isOpen(row, col));
            StdOut.println("Open [" + row + ", " + col + "]");
            model.open(row, col);
        } while (!model.percolates());

        StdOut.println("Percolate with " + model.numberOfOpenSites() + " Open Site");
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        if (row < 1 || row > modelSize)
            throw new IllegalArgumentException("Invalid Row Index");

        if (col < 1 || col > modelSize)
            throw new IllegalArgumentException("Invalid Column Index");

        return isOpenSite[--row][--col];
    }

    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        if (row < 1 || row > modelSize)
            throw new IllegalArgumentException("Invalid Row Index");

        if (col < 1 || col > modelSize)
            throw new IllegalArgumentException("Invalid Column Index");

        if (isOpen(row, col)) return;

        int siteIndex = convertIndexForUF(row, col);

        if (row == 1) {
            siteUf.union(siteIndex, topIndex);
            siteUfForFull.union(siteIndex, topIndex);
        }
        else if (isOpen(row - 1, col)) {
            siteUf.union(siteIndex, siteIndex - modelSize);
            siteUfForFull.union(siteIndex, siteIndex - modelSize);
        }


        if (row == modelSize)
            siteUf.union(siteIndex, bottomIndex);
        else if (isOpen(row + 1, col)) {
            siteUf.union(siteIndex, siteIndex + modelSize);
            siteUfForFull.union(siteIndex, siteIndex + modelSize);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            siteUf.union(siteIndex, siteIndex - 1);
            siteUfForFull.union(siteIndex, siteIndex - 1);
        }


        if (col < modelSize && isOpen(row, col + 1)) {
            siteUf.union(siteIndex, siteIndex + 1);
            siteUfForFull.union(siteIndex, siteIndex + 1);
        }

        isOpenSite[--row][--col] = true;
        openSitesCount++;

        return;
    }

    public boolean percolates()              // does the system percolate?
    {
        return siteUf.connected(topIndex, bottomIndex);
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return openSitesCount;
    }

    private int convertIndexForUF(int row, int col) // Convert 2D indices to 1D for union find.
    {
        return modelSize * (row - 1) + col - 1;
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        if (row < 1 || row > modelSize)
            throw new IllegalArgumentException("Invalid Row Index");

        if (col < 1 || col > modelSize)
            throw new IllegalArgumentException("Invalid Column Index");

        int siteIndex = convertIndexForUF(row, col);

        return siteUfForFull.connected(siteIndex, topIndex);
    }
}
