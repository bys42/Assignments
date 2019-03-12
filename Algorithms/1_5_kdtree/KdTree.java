/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/02/07
 *  Description: nearest/reange search implementation with K-d tree
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root = null;
    private int pointCount = 0;

    // construct an empty set of points
    public KdTree() {
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
        double minX = 0, minY = 0, maxX = 1, maxY = 1;
        if (root == null) {
            root = new Node(p, minX, minY, maxX, maxY);
            pointCount++;
            return;
        }
        Node node = root;
        boolean isVertical = true;
        double cmp;
        while (true) {
            cmp = isVertical ? node.pt.x() - p.x() : node.pt.y() - p.y();
            if (cmp > 0) {
                if (node.lb == null) {
                    if (isVertical) {
                        maxX = node.pt.x();
                        maxY = node.maxY;
                    }
                    else {
                        maxX = node.maxX;
                        maxY = node.pt.y();
                    }
                    node.lb = new Node(p, node.minX, node.minY, maxX, maxY);
                    pointCount++;
                    return;
                }
                node = node.lb;
            }
            else if (!p.equals(node.pt)) {
                if (node.rt == null) {
                    if (isVertical) {
                        minX = node.pt.x();
                        minY = node.minY;
                    }
                    else {
                        minX = node.minX;
                        minY = node.pt.y();
                    }
                    node.rt = new Node(p, minX, minY, node.maxX, node.maxY);
                    pointCount++;
                    return;
                }
                node = node.rt;
            }
            else {
                return;
            }
            isVertical = !isVertical;
        }

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointCount == 0) return false;
        Node node = root;
        boolean isVertical = true;
        double cmp;
        while (true) {
            cmp = isVertical ? node.pt.x() - p.x() : node.pt.y() - p.y();
            isVertical = !isVertical;
            if (cmp > 0) {
                if (node.lb == null) return false;
                node = node.lb;
            }
            else {
                if (p.equals(node.pt)) return true;
                if (node.rt == null) return false;
                node = node.rt;
            }
        }
    }

    private void drawSubtree(Node node, boolean isVertical) {
        StdDraw.setPenRadius(0.005);
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.pt.x(), node.minY, node.pt.x(), node.maxY);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.minX, node.pt.y(), node.maxX, node.pt.y());
        }

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.pt.draw();

        if (node.rt != null) drawSubtree(node.rt, !isVertical);
        if (node.lb != null) drawSubtree(node.lb, !isVertical);
    }

    // draw all points to standard draw
    public void draw() {
        drawSubtree(root, true);
    }

    // all points that are inside the rectangle (or on the boundary)
    private void searchSubtree(RectHV rect, Node node, Queue<Point2D> record) {
        if (node == null) return;

        if (rect.xmax() < node.minX || node.maxX < rect.xmin()) return;
        if (rect.ymax() < node.minY || node.maxY < rect.ymin()) return;

        if (rect.contains(node.pt)) record.enqueue(node.pt);
        searchSubtree(rect, node.lb, record);
        searchSubtree(rect, node.rt, record);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> record = new Queue<>();
        searchSubtree(rect, root, record);
        return record;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    private void searchSubtree(Point2D p, Node node, Nearest nearest) {
        if (node == null) return;

        RectHV rect = new RectHV(node.minX, node.minY, node.maxX, node.maxY);
        if (rect.distanceSquaredTo(p) > nearest.distRecord) return;

        double nodeDistRecord = p.distanceSquaredTo(node.pt);
        if (nodeDistRecord < nearest.distRecord) {
            nearest.pt = node.pt;
            nearest.distRecord = nodeDistRecord;
        }

        searchSubtree(p, node.lb, nearest);
        searchSubtree(p, node.rt, nearest);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointCount == 0) return null;

        Nearest nearest = new Nearest();
        nearest.pt = root.pt;
        nearest.distRecord = p.distanceSquaredTo(root.pt);

        searchSubtree(p, root, nearest);

        return nearest.pt;
    }

    private class Nearest {
        private Point2D pt;
        private double distRecord;
    }

    private static class Node {
        private final Point2D pt;      // the point
        private final double minX;    // the axis-aligned rectangle corresponding to this node
        private final double minY;    // the axis-aligned rectangle corresponding to this node
        private final double maxX;    // the axis-aligned rectangle corresponding to this node
        private final double maxY;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        Node(Point2D p, double xStart, double yStart, double xEnd, double yEnd) {
            pt = p;
            minX = xStart;
            minY = yStart;
            maxX = xEnd;
            maxY = yEnd;
            lb = null;
            rt = null;
        }
    }
}
