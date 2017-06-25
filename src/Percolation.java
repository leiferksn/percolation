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
        int maxIndex = dimensions * dimensions;

        weightedQuickUnionUF = new WeightedQuickUnionUF(dimensions * dimensions + 2);
        virtualTop = 0;
        virtualBottom = maxIndex + 1;

        // init
        grid = new int[dimensions + 1][dimensions + 1];
        for (int i = 1; i <= dimensions; i++) {
            for (int j = 1; j <= dimensions; j++) {
                grid[i][j] = 0;
            }
        }

        for (int i = 1; i <= dimensions; i++) {
            weightedQuickUnionUF.union(virtualTop, 1 * i);
        }

        for (int j = maxIndex; j > maxIndex - dimensions; j--) {
            weightedQuickUnionUF.union(virtualBottom, j);
        }
    }

    public void open(int row, int col) {
        if (row * col < 0 ||  row * col > dimensions * dimensions) {
            throw new IllegalArgumentException("You are trying to open non existing site.");
        }

        if (!withinRanges(row, col)) {
            throw new IndexOutOfBoundsException("Row or Column is outside grid.");
        }

        if (!isOpen(row, col)) {
            grid[row][col] = 1;
            numberOfOpenSites++;
            int element = getArrayElementByCoordinates(row, col);

            int left = getArrayElementByCoordinates(row, col-1);
            int right = getArrayElementByCoordinates(row,  col + 1);
            int up = getArrayElementByCoordinates(row - 1, col);
            int down = getArrayElementByCoordinates(row + 1, col);

            if (col > 1 && isOpen(row, col -1) && !weightedQuickUnionUF.connected(element, left)) {
                weightedQuickUnionUF.union(element, left);
            }

            if (row > 1 && isOpen(row - 1, col) && !weightedQuickUnionUF.connected(element, up)) {
                weightedQuickUnionUF.union(element, up);
            }

            if (col < dimensions && isOpen(row, col + 1) && !weightedQuickUnionUF.connected(element, right)) {
                    weightedQuickUnionUF.union(element, right);
            }

            if (row < dimensions && isOpen(row + 1, col) && !weightedQuickUnionUF.connected(element, down)) {
                weightedQuickUnionUF.union(element, down);
            }
        }
    }

    private int getArrayElementByCoordinates(int x, int y) {
        return (x - 1) * dimensions + y;
    }

    public boolean isOpen(int row, int col) {
        if (!withinRanges(row, col)) {
            throw new IndexOutOfBoundsException("Row or Column is outside grid.");
        }
        return grid[row][col] == 1;
    }

    /**
     * for visualization to work for all cases, we need a second
     * weightedQuickUnionUF, without virtual nodes
     */
    public boolean isFull(int row, int col) {
        if (!withinRanges(row, col)) {
            throw new IndexOutOfBoundsException("Row or Column is outside of grid.");
        }
        int element = getArrayElementByCoordinates(row, col);
        for (int i = 1; i <= dimensions; i++) {
            if (isOpen(1, i) && isOpen(row, col)
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
        return weightedQuickUnionUF.connected(virtualTop, virtualBottom);
    }

    private boolean withinRanges(int row, int col) {
        if (row < 0 || row > dimensions || col < 0 || col > dimensions) {
            return false;
        }
        return true;
    }
}
