import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openNum = 0;
    private final boolean[][] gridOpen; // open or not
    private final WeightedQuickUnionUF ufUp; // why could this be final?
    private final WeightedQuickUnionUF ufDn;
    private final int sN;
    private final int upSite;
    private final int dnSite;
    private boolean ifPercolate = false;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be lager than 0!");
        }
        gridOpen = new boolean[n][n];
        ufUp = new WeightedQuickUnionUF(n * n + 2);
        ufDn = new WeightedQuickUnionUF(n * n + 2);
        upSite = 0;
        dnSite = n * n + 1;
        sN = n;

    }

    private void validateIdx(int row, int col) {
        if (row <= 0 || row > sN || col <= 0 || col > sN) {
            System.out.println(row);
            System.out.println(col);
            throw new IllegalArgumentException(" row and col should be within [1,n]!");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        validateIdx(row, col);
        if (gridOpen[row - 1][col - 1]) {
            return;
        }
        gridOpen[row - 1][col - 1] = true;
        openNum += 1;

        int curId = xyToId(row, col); // row-1, col-1
        int upId = xyToId(row - 1, col);  // row-2, col-1
        int downId = xyToId(row + 1, col);    // row , col -1
        int lfId = xyToId(row, col - 1); // row-1, col-2
        int rtId = xyToId(row, col + 1); // row-1, col
        // System.out.println(ufUp.find(curId));

        // up
        if (row == 1) {
            ufUp.union(upSite, curId);
        }
        else if (gridOpen[row - 2][col - 1]) {
            ufUp.union(upId, curId);
            ufDn.union(upId, curId);
        }
        // left
        if (col > 1 && gridOpen[row - 1][col - 2]) {
            ufUp.union(lfId, curId);
            ufDn.union(lfId, curId);
        }
        // right
        if (col < sN && gridOpen[row - 1][col]) {
            ufUp.union(rtId, curId);
            ufDn.union(rtId, curId);
        }
        // down
        if (row == sN) {
            ufDn.union(dnSite, curId);
        }
        else if (row < sN && gridOpen[row][col - 1]) {
            ufUp.union(downId, curId);
            ufDn.union(downId, curId);
        }

        if (isFullDown(row, col) && isFull(row, col)) {
            ifPercolate = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIdx(row, col);
        if (gridOpen[row - 1][col - 1]) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIdx(row, col);
        if (ufUp.find(upSite) == ufUp.find(xyToId(row, col))) {
            return true;
        }
        return false;
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return ifPercolate;
    }

    private boolean isFullDown(int row, int col) {
        validateIdx(row, col);
        if (ufDn.find(dnSite) == ufDn.find(xyToId(row, col))) {
            return true;
        }
        return false;
    }

    private int xyToId(int row, int col) {
        return (row - 1) * sN + col;
    }

    // test client (optional)
    public static void main(String[] args) {

        // System.out.println(Arrays.toString(StdRandom.permutation(5, 2)));
        Percolation prc = new Percolation(5);
        prc.open(1, 1);
        prc.open(1, 2);
        prc.open(1, 2);
        prc.open(3, 5);
        prc.open(4, 5);
        prc.open(5, 5);
        prc.open(3, 4);
        prc.open(2, 2);
        prc.open(3, 3);
        // PercolationVisualizer.draw(prc, 5);
        // StdDraw.pause(14);
        System.out.println(prc.numberOfOpenSites());
        // System.out.println(prc.isFull(0, 5));
        System.out.println(prc.percolates());

    }


}
