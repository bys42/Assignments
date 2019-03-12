/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/25
 *  Description: DAG Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet localWorldNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        localWorldNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int dist = 0;
        String outcastStr = null;
        for (String noun : nouns) {
            int nounDist = 0;
            for (String anotherNoun : nouns) {
                nounDist += localWorldNet.distance(noun, anotherNoun);
            }
            if (nounDist > dist) {
                dist = nounDist;
                outcastStr = noun;
            }
        }
        return outcastStr;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
