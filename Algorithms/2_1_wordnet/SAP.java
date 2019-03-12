/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/25
 *  Description: DAG Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private BreadthFirstDirectedPaths vBFDP;
    private BreadthFirstDirectedPaths wBFDP;
    private int recordV = -1;
    private int recordW = -1;
    private int recordAncestor;
    private int recordLength;
    private Iterable<Integer> recordIterV = new Queue<Integer>();
    private Iterable<Integer> recordIterW = new Queue<Integer>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G.V());
        for (int i = 0; i < G.V(); i++) {
            for (int v : G.adj(i)) {
                digraph.addEdge(i, v);
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return ancestor(v, w) != -1 ? recordLength : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0) throw new IllegalArgumentException();

        if (recordV == v && recordW == w) return recordAncestor;

        if (recordV != v) {
            vBFDP = new BreadthFirstDirectedPaths(digraph, v);
            recordV = v;
            recordIterV = new Queue<Integer>();
        }

        if (recordW != w) {
            wBFDP = new BreadthFirstDirectedPaths(digraph, w);
            recordW = w;
            recordIterW = new Queue<Integer>();
        }

        return ancestorHelper();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestor(v, w) != -1 ? recordLength : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer vertex : v) {
            if (vertex == null || vertex < 0) throw new IllegalArgumentException();
        }
        for (Integer vertex : w) {
            if (vertex == null || vertex < 0) throw new IllegalArgumentException();
        }

        if (recordIterV.equals(v) && recordIterW.equals(w)) return recordAncestor;

        if (!recordIterV.equals(v)) {
            vBFDP = new BreadthFirstDirectedPaths(digraph, v);
            recordIterV = v;
            recordV = -1;
        }

        if (!recordIterW.equals(w)) {
            wBFDP = new BreadthFirstDirectedPaths(digraph, w);
            recordIterW = w;
            recordW = -1;
        }

        return ancestorHelper();
    }

    private int ancestorHelper() {
        recordAncestor = -1;
        recordLength = Integer.MAX_VALUE;

        for (int v = 0; v < digraph.V(); v++) {
            if (!vBFDP.hasPathTo(v) || !wBFDP.hasPathTo(v)) continue;
            int distV = vBFDP.distTo(v);
            int distW = wBFDP.distTo(v);
            if (distV + distW < recordLength) {
                recordLength = distV + distW;
                recordAncestor = v;
            }
        }
        return recordAncestor;
    }
}
