/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/04/08
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {
    private static final int ASCII_MAX = 256;
    private static final int CHAR_SIZE = 8;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray CSA = new CircularSuffixArray(str);

        int firstIdx = 0;
        int strLength = CSA.length();

        while (CSA.index(firstIdx) != 0) {
            firstIdx++;
        }
        BinaryStdOut.write(firstIdx);

        for (int i = 0; i < strLength; i++) {
            int t = CSA.index(i);
            if (t == 0) {
                t = strLength - 1;
            }
            else {
                t--;
            }
            BinaryStdOut.write(str.charAt(t), CHAR_SIZE);
        }
        BinaryStdOut.close();
    }


    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int firstIdx = BinaryStdIn.readInt();

        String str = BinaryStdIn.readString();
        char[] input = str.toCharArray();
        int length = input.length;
        Map<Character, Queue<Integer>> map = new HashMap<>();
        char maxChar = 0;
        int[] count = new int[ASCII_MAX + 1];

        for (int i = 0; i < length; i++) {
            char c = input[i];
            count[c + 1]++;
            if (c > maxChar) maxChar = c;
            if (!map.containsKey(c)) {
                map.put(c, new Queue<Integer>());
            }
            map.get(c).enqueue(i);
        }

        for (int i = 0; i <= maxChar; i++) {
            count[i + 1] += count[i];
        }

        char[] aux = new char[length];
        int[] next = new int[length];
        for (int i = 0; i < length; i++) {
            char c = input[i];
            int idx = count[c]++;
            aux[idx] = c;
            next[idx] = map.get(c).dequeue();
        }

        for (int i = 0, idx = firstIdx; i < length; i++) {
            BinaryStdOut.write(aux[idx]);
            idx = next[idx];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if ((args[0].equals("+"))) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
