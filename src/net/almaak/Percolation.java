package net.almaak;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private  int dimensions = 0;
    private int numberOfOpenSites = 0;
    private int virtualTop = 0;
    private int virtualBottom = 0;
    private int[][] grid = null;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Dimensions can't be negative");
        }

        dimensions = n;
        int maxIndex = dimensions * dimensions - 1;

        weightedQuickUnionUF = new WeightedQuickUnionUF(dimensions * dimensions + 2);
        virtualTop = maxIndex + 1;
        virtualBottom = maxIndex + 2;

        // init
        grid = new int[dimensions][dimensions];
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                grid[i][j] = 0;
            }
        }

        for (int i = 0; i < dimensions; i++) {
            weightedQuickUnionUF.union(virtualTop, 1 * i);
        }

        for (int j = maxIndex; j > maxIndex - dimensions; j--) {
            weightedQuickUnionUF.union(virtualBottom, j);
        }
    }

    public void open(int row, int col) {
        withinRanges(row, col);

        if (row * col < 0 ||  row * col > dimensions * dimensions) {
            throw new IllegalArgumentException("You are trying to open non existing site");
        }
        if (!isOpen(row, col)) {
            grid[row][col] = 1;
            numberOfOpenSites++;
            int element = getArrayElementByCoordinates(row, col);

            int left = getArrayElementByCoordinates(row, col-1);
            int right = getArrayElementByCoordinates(row,  col + 1);
            int up = getArrayElementByCoordinates(row - 1, col);
            int down = getArrayElementByCoordinates(row + 1, col);

            if (col > 0 && isOpen(row, col -1 ) && !weightedQuickUnionUF.connected(element, left)) {
                weightedQuickUnionUF.union(element, left);
            }

            if (row > 0 && isOpen(row - 1, col) && !weightedQuickUnionUF.connected(element, up)) {
                weightedQuickUnionUF.union(element, up);
            }

            if (col < dimensions - 1 && isOpen(row, col + 1) && !weightedQuickUnionUF.connected(element, right)) {
                    weightedQuickUnionUF.union(element, right);
            }

            if (row < dimensions - 1 && isOpen(row + 1, col) && !weightedQuickUnionUF.connected(element, down)) {
                weightedQuickUnionUF.union(element, down);
            }
        }
    }

    private int getArrayElementByCoordinates(int x, int y) {
        return x * dimensions + y;
    }

    public boolean isOpen(int row, int col) {
        withinRanges(row, col);
        return grid[row][col] == 1;
    }

    public boolean isFull(int row, int col) {
        withinRanges(row, col);
        int element = getArrayElementByCoordinates(row, col);
        for (int i = 0; i < dimensions; i++) {
            if (isOpen(0, i) && isOpen(row, col)
                    && weightedQuickUnionUF.connected(element, i)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(virtualTop, virtualBottom) ;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        long start = System.currentTimeMillis();
        Percolation percolation = new Percolation(n);

        while (!percolation.percolates()) {
            int random = StdRandom.uniform(0, n * n - 1);
            int row = (int)Math.floor((double)random / n);
            int col = random - row * n;
            percolation.open(row, col);
        }

        long end = System.currentTimeMillis() - start;
        printSites(percolation, end);

    }

    private boolean withinRanges(int row, int col) {
        if (row < 0 || row > dimensions - 1 || col < 0 || col > dimensions - 1) {
            return false;
        }
        return true;
    }

    private static void printSites(Percolation percolation, long timeToFinish) {
        int dimensions = percolation.dimensions;
        for (int i = 0; i < dimensions; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < dimensions; j++) {
                sb.append(" ");
                if (percolation.isOpen(i,j)) {
                    sb.append("◼");
                } else {
                    sb.append("◻");
                }
            }
            StdOut.println(sb.toString());
        }
        double threshold =  (double) percolation.numberOfOpenSites() / (dimensions * dimensions);
        StdOut.println(percolation.numberOfOpenSites() + " [ " + threshold + " ]" + " in " + ((double)timeToFinish / 1000 ) + " sec.");
    }
}
