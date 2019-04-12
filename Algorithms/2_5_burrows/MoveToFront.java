/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/04/08
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int ASCII_MAX = 256;
    private static final int CHAR_SIZE = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String str = BinaryStdIn.readString();
        char[] input = str.toCharArray();

        int[] charToIdx = new int[ASCII_MAX];
        char[] idxToChar = new char[ASCII_MAX];
        for (int i = 0; i < ASCII_MAX; i++) {
            charToIdx[i] = i;
            idxToChar[i] = (char) i;
        }

        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            BinaryStdOut.write(charToIdx[c], CHAR_SIZE);
            for (int j = charToIdx[c]; j > 0; j--) {
                char preChar = idxToChar[j - 1];
                charToIdx[preChar] = j;
                idxToChar[j] = preChar;
            }
            charToIdx[c] = 0;
            idxToChar[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        String str = BinaryStdIn.readString();
        char[] input = str.toCharArray();

        char[] idxToChar = new char[ASCII_MAX];
        for (int i = 0; i < ASCII_MAX; i++) {
            idxToChar[i] = (char) i;
        }

        for (int i = 0; i < input.length; i++) {
            int idx = input[i];
            char c = idxToChar[idx];
            BinaryStdOut.write(c, CHAR_SIZE);
            for (int j = idx; j > 0; j--) {
                idxToChar[j] = idxToChar[j - 1];
            }
            idxToChar[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if ((args[0].equals("+"))) {
            decode();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
