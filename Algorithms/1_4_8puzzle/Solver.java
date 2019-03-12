/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/03
 *  Description: assignment: puzzle solver
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solutionStack;
    private final int movesAns;

    public Solver(Board initial) // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null) throw new IllegalArgumentException("initial is null");

        solutionStack = new Stack<>();

        if (initial.isGoal()) {
            solutionStack.push(initial);
            movesAns = 0;
            return;
        }
        else if (initial.twin().isGoal()) {
            movesAns = -1;
            return;
        }

        MinPQ<Node> searchNode = new MinPQ<>();
        Node currentNode = new Node(initial, 0, null);
        for (Board neighbor : initial.neighbors()) {
            searchNode.insert(new Node(neighbor, 1, currentNode));
        }

        do {
            currentNode = searchNode.delMin();
            for (Board neighbor : currentNode.board.neighbors()) {
                if (neighbor.equals(currentNode.predecessor.board))
                    continue;
                searchNode.insert(new Node(neighbor, currentNode.move + 1, currentNode));
            }
        } while (!currentNode.board.isGoal() && !currentNode.board.twin().isGoal());
        if (currentNode.board.isGoal()) {
            movesAns = currentNode.move;
            while (currentNode != null) {
                solutionStack.push(currentNode.board);
                currentNode = currentNode.predecessor;
            }
        }
        else {
            movesAns = -1;
        }
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    public boolean isSolvable() // is the initial board solvable?
    {
        return movesAns != -1;
    }

    public int moves() // min number of moves to solve initial board; -1 if unsolvable
    {
        return movesAns;
    }

    public Iterable<Board> solution() // sequence of boards in a shortest solution; null if unsolvable
    {
        return isSolvable() ? solutionStack : null;
    }

    private class Node implements Comparable<Node> {
        Board board;
        int move;
        Node predecessor;

        Node(Board board, int move, Node predecessor) {
            this.board = board;
            this.move = move;
            this.predecessor = predecessor;
        }

        public int compareTo(Node that) {
            int priorityThis = move + board.manhattan();
            int priorityThat = that.move + that.board.manhattan();
            return priorityThis - priorityThat;
        }
    }
}
