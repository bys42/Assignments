/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/04/08
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int stringLength;
    private final Integer[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        stringLength = s.length();
        String str = s + s;

        index = new Integer[stringLength];
        for (int i = 0; i < stringLength; i++) {
            index[i] = i;
        }

        Arrays.sort(index, (i, j) -> {
            int length = 0;
            while (str.charAt(i) == str.charAt(j)) {
                i++;
                j++;
                length++;
                if (length == stringLength) return 0;
            }
            return str.charAt(i) - str.charAt(j);
        });
    }

    // length of s
    public int length() {
        return stringLength;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= stringLength) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        StdOut.println("csa.length() = " + csa.length());
        String str = args[0] + args[0];
        for (int i = 0; i < args[0].length(); i++) {
            int t = csa.index(i);
            StdOut.println(str.substring(t, t + csa.length()));
        }
    }
}
