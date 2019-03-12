/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/23
 *  Description: FastCollinearPoints Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final Node segmentLink;
    private final int segmentsCount;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new IllegalArgumentException("argument is null");

        Point[] sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("find null point");
            sortedPoints[i] = points[i];
        }

        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("find repeated point");
            }
        }

        Node record = new Node(null);
        int count = 0;
        for (int i = 0; i < points.length - 3; i++) {
            boolean collinearFind = false;
            Arrays.sort(sortedPoints);
            Point origin = sortedPoints[i];
            Arrays.sort(sortedPoints, origin.slopeOrder());
            for (int j = points.length - 1; j > 1; j--) {
                if (origin.slopeTo(sortedPoints[j]) != origin.slopeTo(sortedPoints[j - 2])) {
                    if (collinearFind && origin.compareTo(sortedPoints[j - 1]) < 0) {
                        record = new Node(record);
                        count++;
                    }
                    collinearFind = false;
                    continue;
                }
                if (!collinearFind) {
                    collinearFind = true;
                    record.lineSegment = new LineSegment(origin, sortedPoints[j]);
                }
            }
        }
        segmentLink = record;
        segmentsCount = count;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segmentStore = new LineSegment[segmentsCount];
        Node segmentNode = segmentLink;
        for (int i = 0; i < segmentsCount; i++) {
            segmentNode = segmentNode.next;
            segmentStore[i] = segmentNode.lineSegment;
        }
        return segmentStore;
    }

    public int numberOfSegments()        // the number of line segments
    {
        return segmentsCount;
    }

    private class Node {
        LineSegment lineSegment;
        Node next;

        Node(Node next) {
            this.next = next;
            return;
        }
    }
}
