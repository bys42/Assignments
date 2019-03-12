/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/03
 *  Description: assignment: board instance(?)
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int dim;
    private final int hammingValue;
    private final int manhattanValue;
    private final String boardString;
    private final char[] blocksCopy;
    private final int blankRow;
    private final int blankColumn;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        if (blocks == null) throw new IllegalArgumentException("blocks is null");

        int trans = 0;
        int hammingLocal = 0;
        int manhattanLocal = 0;
        int blankRowLocal = 0;
        int blankColumnLocal = 0;

        StringBuilder stringBuilder = new StringBuilder();
        dim = blocks.length;
        blocksCopy = new char[dim * dim];

        stringBuilder.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int val = blocks[i][j];
                blocksCopy[trans] = (char) val;
                stringBuilder.append(String.format("%2d ", val));
                if (val == 0) {
                    blankRowLocal = i;
                    blankColumnLocal = j;
                }
                else if (val != trans + 1) {
                    hammingLocal++;
                    manhattanLocal += Math.abs((val - 1) / dim - i) + Math.abs((val - 1) % dim - j);
                }
                trans++;
            }
            stringBuilder.append("\n");
        }
        this.blankRow = blankRowLocal;
        this.blankColumn = blankColumnLocal;
        this.hammingValue = hammingLocal;
        this.manhattanValue = manhattanLocal;
        boardString = stringBuilder.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println("toString: \n" + initial.toString());
            StdOut.println("dimension: " + initial.dimension());
            StdOut.println("hamming: " + initial.hamming());
            StdOut.println("manhattan: " + initial.manhattan());
            StdOut.println("isGoal: " + initial.isGoal());
            StdOut.println("twin:\n" + initial.twin().toString());
            for (Board neighbor : initial.neighbors()) {
                StdOut.println("neighbors:\n" + neighbor.toString());
            }

        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
        return dim;
    }

    public int hamming()                   // number of blocks out of place
    {
        return hammingValue;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattanValue;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return hammingValue == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] blocksTwin = new int[dim][dim];

        int trans = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                blocksTwin[i][j] = blocksCopy[trans++];
            }
        }

        int twinRow = blankRow > 0 ? blankRow - 1 : blankRow + 1;
        int twinColumn = blankColumn > 0 ? blankColumn - 1 : blankColumn + 1;

        int tmp = blocksTwin[blankRow][twinColumn];

        blocksTwin[blankRow][twinColumn] = blocksTwin[twinRow][blankColumn];
        blocksTwin[twinRow][blankColumn] = tmp;

        return new Board(blocksTwin);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        return boardString.equals(y.toString());
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        return boardString;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> queueNeighbor = new Queue<>();

        int[][] neighbor = new int[dim][dim];

        int trans = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                neighbor[i][j] = blocksCopy[trans++];
            }
        }

        if (blankRow > 0) {
            neighbor[blankRow][blankColumn] = neighbor[blankRow - 1][blankColumn];
            neighbor[blankRow - 1][blankColumn] = 0;
            queueNeighbor.enqueue(new Board(neighbor));
            neighbor[blankRow - 1][blankColumn] = neighbor[blankRow][blankColumn];
            neighbor[blankRow][blankColumn] = 0;
        }
        if (blankRow < dim - 1) {
            neighbor[blankRow][blankColumn] = neighbor[blankRow + 1][blankColumn];
            neighbor[blankRow + 1][blankColumn] = 0;
            queueNeighbor.enqueue(new Board(neighbor));
            neighbor[blankRow + 1][blankColumn] = neighbor[blankRow][blankColumn];
            neighbor[blankRow][blankColumn] = 0;
        }
        if (blankColumn > 0) {
            neighbor[blankRow][blankColumn] = neighbor[blankRow][blankColumn - 1];
            neighbor[blankRow][blankColumn - 1] = 0;
            queueNeighbor.enqueue(new Board(neighbor));
            neighbor[blankRow][blankColumn - 1] = neighbor[blankRow][blankColumn];
            neighbor[blankRow][blankColumn] = 0;
        }
        if (blankColumn < dim - 1) {
            neighbor[blankRow][blankColumn] = neighbor[blankRow][blankColumn + 1];
            neighbor[blankRow][blankColumn + 1] = 0;
            queueNeighbor.enqueue(new Board(neighbor));
            neighbor[blankRow][blankColumn + 1] = neighbor[blankRow][blankColumn];
            neighbor[blankRow][blankColumn] = 0;
        }

        return queueNeighbor;
    }
}
