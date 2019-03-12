/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/07
 *  Description: Brute-force nearest/reange search implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointTree = new TreeSet<>();
    private int pointCount = 0;

    // construct an empty set of points
    public PointSET() {
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

    // is the set empty?
    public boolean isEmpty() {
        return (pointCount == 0);
    }

    // number of points in the set
    public int size() {
        return pointCount;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointTree.contains(p)) {
            return;
        }
        pointTree.add(p);
        pointCount++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointTree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D q : pointTree) {
            q.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> rectPoints = new Queue<>();
        for (Point2D q : pointTree) {
            if (rect.contains(q)) {
                rectPoints.enqueue(q);
            }
        }
        return rectPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D nearestPoint = null;
        double nearestDistSquare = 2;
        for (Point2D q : pointTree) {
            if (q.distanceSquaredTo(p) <= nearestDistSquare) {
                nearestDistSquare = q.distanceSquaredTo(p);
                nearestPoint = q;
            }
        }
        return nearestPoint;
    }
}
