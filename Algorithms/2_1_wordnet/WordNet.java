/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/25
 *  Description: DAG Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private final Map<Integer, String> idToNouns = new HashMap<>();
    private final Map<String, Queue<Integer>> nounToIds = new HashMap<>();
    private final SAP wordNetSap;
    private boolean rootFound = false;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        int synsetId = 0;
        In synsetsFile = new In(synsets);

        while (!synsetsFile.isEmpty()) {
            String currentLine = synsetsFile.readLine();
            String nounsString = currentLine.split(",")[1];

            idToNouns.put(synsetId, nounsString);
            String[] nounArray = nounsString.split(" ");
            for (String noun : nounArray) {
                Queue<Integer> ids = nounToIds.get(noun);
                if (ids == null) {
                    ids = new Queue<Integer>();
                    nounToIds.put(noun, ids);
                }
                ids.enqueue(synsetId);
            }
            synsetId++;
        }

        Digraph wordNet = new Digraph(synsetId);
        In hypernymsFile = new In(hypernyms);

        while (!hypernymsFile.isEmpty()) {
            String currentLine = hypernymsFile.readLine();
            String[] stringItem = currentLine.split(",");
            synsetId = Integer.parseInt(stringItem[0]);
            for (int i = 1; i < stringItem.length; i++) {
                wordNet.addEdge(synsetId, Integer.parseInt(stringItem[i]));
            }
        }

        checkRootedDag(wordNet);

        wordNetSap = new SAP(wordNet);
    }

    private void dfs(Digraph G, int v, boolean[] marked, boolean[] onStack) {
        onStack[v] = true;
        marked[v] = true;
        if (!G.adj(v).iterator().hasNext()) {
            if (rootFound) throw new IllegalArgumentException();
            rootFound = true;
        }
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w, marked, onStack);
            else if (onStack[w]) throw new IllegalArgumentException();
        }
        onStack[v] = false;
    }

    private void checkRootedDag(Digraph G) {
        boolean[] marked = new boolean[G.V()];
        boolean[] onStack = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v, marked, onStack);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return wordNetSap.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToIds.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int ancestorId = wordNetSap.ancestor(nounToIds.get(nounA), nounToIds.get(nounB));
        return ancestorId == -1 ? null : idToNouns.get(ancestorId);
    }
}
