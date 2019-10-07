package point.convexhull;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import point.PlotPane;
import point.Point;

import java.util.*;

/**
 * Created by Z on 09.08.
 * Introduction to Java Programming, 10th Edition
 * Chapter  22: Developing Efficient Algorithms
 *          22.10: Computational Geometry: Finding a Convex Hull
 *
 * This class represents a pane that extends that PlotPane class
 * and that supports some additional functions for finding the convex
 * hull of a set of points.
 *
 * @see PlotPane
 */
public final class ConvexHullPane extends PlotPane {

    public ConvexHullPane() {
        super();
    }

    public ConvexHullPane(double width, double height) {
        super(width, height);
    }

    public void solve() {
        List<Point> convex = getConvexHull(points);
        for (int i = 0, j = 1; j < convex.size(); i++, j++) {
            Point p1 = convex.get(i);
            Point p2 = convex.get(j);

            Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            line.setStroke(Color.DARKSLATEGRAY);
            getChildren().add(line);
        }

        Line l = new Line(
                convex.get(0).getX(),
                convex.get(0).getY(),
                convex.get(convex.size() - 1).getX(),
                convex.get(convex.size() - 1).getY()
        );
        l.setStroke(Color.DARKSLATEGRAY);
        getChildren().add(l);
    }

    private List<Point> getConvexHull(List<Point> points) {
        if (points.size() < 2)
            throw new IllegalArgumentException("size < 2");

        List<Point> convexHull = new LinkedList<>();
        Point start;
        convexHull.add(start = getRightmostLowest(points));

        // all points of the convex hull should be added to the list
        // after this loop
        Point p2 = start;
        do {
            Point p1 = p2;
            p2 = start;

            // p2 should be the next point in the convex hull after this loop
            for (Point p : points) {
                // >0 left; =0 on-line; <0 right
                double direction = (p2.getX() - p1.getX()) * (p.getY() - p1.getY()) -
                        (p.getX() - p1.getX()) * (p2.getY() - p1.getY());

                if (direction > 0 || (direction == 0 && p1.distance(p) > p1.distance(p2)))
                    p2 = p;
            }

            convexHull.add(p2); // add t1 to the convex hull list
        } while (!p2.equals(start));

        return convexHull;
    }

    private List<Point> getConvexHull2(List<Point> points) {
        Point start = getRightmostLowest(points);
        List<Point> copy = new ArrayList<>(points);
        copy.sort((p1, p2) -> {
            double dx1 = p1.getX() - start.getX();
            double dy1 = start.getY() - p1.getY();
            double deg1 = (dx1 == 0) ? 90 : Math.toDegrees(Math.atan(dy1 / dx1));
            if (dx1 < 0)
                deg1 = -deg1 + 90;

            double dx2 = p2.getX() - start.getX();
            double dy2 = start.getY() - p2.getY();
            double deg2 = (dx2 == 0) ? 90 : Math.toDegrees(Math.atan(dy2 / dx2));
            if (dx2 < 0)
                deg2 += -deg2 + 90;

            if (deg1 < deg2)
                return -1;
            else if (deg2 < deg1)
                return 1;
            return 0;
        });

        Deque<Point> convexHull = new LinkedList<>();
        convexHull.addFirst(copy.get(0));
        convexHull.addFirst(copy.get(1));
        convexHull.addFirst(copy.get(2));

        int i = 3;
        while (i < copy.size()) {
            Point p1 = convexHull.removeFirst();
            Point p2 = convexHull.removeFirst();

            // todo step 4: if p[i] is on the left of the direct line from p1 to p2
        }

        return new ArrayList<>(convexHull);
    }

    private Point getRightmostLowest(List<Point> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("empty list");

        Comparator<Point> c = (p1, p2) -> {
            int dy = Point.COMPARE_Y.reversed().compare(p1, p2);
            if (dy == 0) {
                if (p1.getX() < p2.getX())
                    return 1;
                else if (p2.getX() < p1.getX())
                    return -1;
                return 0;
            }
            return dy;
        };

        Iterator<Point> itr = list.iterator();
        Point p = itr.next();
        while (itr.hasNext()) {
            Point next = itr.next();
            if (c.compare(next, p) < 0)
                p = next;
        }

        return p;
    }
}
