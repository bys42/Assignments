/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/23
 *  Description: BruteCollinearPoints Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final Node segmentLink;
    private final int segmentsCount;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
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
        Arrays.sort(sortedPoints);

        Node record = new Node(null);
        int count = 0;

        for (int i = 0; i < points.length - 3; i++) {
            Point base = sortedPoints[i];
            compareWithBase:
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int m = j + 1; m < points.length - 1; m++) {
                    if (base.slopeTo(sortedPoints[j]) != base.slopeTo(sortedPoints[m])) continue;

                    for (int n = m + 1; n < points.length; n++) {
                        if (base.slopeTo(sortedPoints[j]) == base.slopeTo(sortedPoints[n])) {
                            count++;
                            record.lineSegment = new LineSegment(base, sortedPoints[n]);
                            record = new Node(record);
                            continue compareWithBase;
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    public int numberOfSegments()        // the number of line segments
    {
        return segmentsCount;
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

    private class Node {
        LineSegment lineSegment;
        Node next;

        Node(Node next) {
            this.next = next;
            return;
        }
    }
}
