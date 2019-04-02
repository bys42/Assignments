/* *****************************************************************************
 *  Name: BYS42
 *  Date: 2019/03/30
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean[][] mark;
    private int rowMax;
    private int colMax;
    private Node root;   // root of TST

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();

        for (String word : dictionary) put(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private boolean contains(String key) {
        Node x = get(root, key, 0);

        return (x != null) && x.isWordEnd;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (x == null) return null;

        char c = key.charAt(d);

        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    private void put(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }
        root = put(root, word, 0);
    }

    private Node put(Node x, String word, int d) {
        char c = word.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, word, d);
        else if (c > x.c) x.right = put(x.right, word, d);
        else if (d < word.length() - 1) x.mid = put(x.mid, word, d + 1);
        else x.isWordEnd = true;
        return x;
    }

    // get nextNode of x which has char = c
    private Node getNode(Node x, char c) {
        if (x == null) return null;

        if (c < x.c) return getNode(x.left, c);
        else if (c > x.c) return getNode(x.right, c);
        return x;
    }

    private void dfs(int row, int col, int d, Node x, Set<String> validWords, BoggleBoard board) {
        char c = board.getLetter(row, col);

        Node curr = getNode(x, c);
        if (curr == null) return;

        if (c == 'Q') {
            curr = getNode(curr.mid, 'U');
            if (curr == null) return;
            stringBuilder.append("QU");
            d++;
        }
        else {
            stringBuilder.append(c);
        }
        mark[row][col] = true;

        if (d > 1 && curr.isWordEnd) {
            validWords.add(stringBuilder.toString());
        }

        int minAdjRow = row == 0 ? 0 : row - 1;
        int maxAdjRow = row == rowMax ? rowMax : row + 1;
        int minAdjCol = col == 0 ? 0 : col - 1;
        int maxAdjCol = col == colMax ? colMax : col + 1;

        for (int i = minAdjRow; i <= maxAdjRow; i++) {
            for (int j = minAdjCol; j <= maxAdjCol; j++) {
                if (mark[i][j]) continue;
                dfs(i, j, d + 1, curr.mid, validWords, board);
            }
        }

        if (c == 'Q') {
            stringBuilder.delete(d - 1, d + 1);
        }
        else {
            stringBuilder.deleteCharAt(d);
        }
        mark[row][col] = false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();

        rowMax = board.rows() - 1;
        colMax = board.cols() - 1;
        Set<String> validWords = new HashSet<>();

        mark = new boolean[rowMax + 1][colMax + 1];
        for (int i = 0; i <= rowMax; i++) {
            for (int j = 0; j <= colMax; j++) {
                dfs(i, j, 0, root, validWords, board);
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        if (!contains(word)) return 0;

        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    private class Node {
        private char c;                 // character
        private Node left, mid, right;  // left, middle, and right subtries
        private boolean isWordEnd;      // value associated with string
    }
}

