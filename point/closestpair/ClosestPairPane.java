package point.closestpair;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import point.PlotPane;
import point.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Z on 09.01.
 * Introduction to Java Programming, 10th Edition
 * Chapter  22: Developing Efficient Algorithms
 *
 * This pane extends PlotPane from the parent package. It carries on
 * the original functions of a plotting pane with additional methods
 * that help finding the closest pair from all plotted points.
 *
 * @see PlotPane
 */
public class ClosestPairPane extends PlotPane {

    /**
     * A class that represents a pair of points. It consists of two
     * Point objects from this package.
     *
     * @see Point
     */
    static final class Pair {

        /**
         * The pair of points.
         */
        private Point p1;
        private Point p2;

        /**
         * Constructs a pair with the given two points.
         *
         * @param p1 point 1
         * @param p2 point 2
         */
        private Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        /**
         * Returns the distance from p1 to p2, calculated using the
         * Pythagorean Theorem.
         *
         * @return the distance between this pair of points
         */
        double distance() {
            double dx = Math.abs(p1.getX() - p2.getX());
            double dy = Math.abs(p1.getY() - p2.getY());
            return Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * Checks if the given point is present in this pair. Point p
         * is considered present if it is equal to one of the points
         * of this pair.
         *
         * @param p the point to check
         * @return  true if this pair contains p; false otherwise
         */
        boolean contains(Point p) {
            return p1.equals(p) || p2.equals(p);
        }

        /**
         * Returns a string representation of this pair. The string
         * consists of the string representation of the two points
         * connected by an arrow ( -> ).
         *
         * @return a string representation of this pair
         */
        @Override
        public String toString() {
            return String.format("%s -> %s", p1, p2);
        }
    }

    public ClosestPairPane() {
        super();
    }

    public ClosestPairPane(double width, double height) {
        super(width, height);
    }

    /**
     * Call add(Point) in superclass and solve again if autoSolve is
     * on.
     *
     * @see PlotPane#add(Point)
     */
    @Override
    public void add(Point p) {
        super.add(p);
        if (autoSolve)
            solve();
    }

    /**
     * Call remove(int) in superclass and remove the closestLine.
     * Solve again if autoSolve is on.
     *
     * @see PlotPane#remove(int)
     */
    @Override
    public void remove(int index) {
        super.remove(index);

        // in case the removing point is one of the points in closest
        getChildren().remove(closestLine);

        // solve for closest if it's on auto-solve
        if (autoSolve)
            solve();
    }

    /**
     * Call clear() in superclass and reset the closest to null.
     *
     * @see PlotPane#clear()
     */
    @Override
    public void clear() {
        super.clear();
        closest = null;
    }

    /**
     * Stores the closest pair of points and provides a getter
     * method for it.
     */
    private Pair closest;

    public Pair getClosest() {
        return closest;
    }

    /**
     * Store the line that connects the closest pair so that the
     * position of the line is manipulable. A remove method is
     * provided for removing the line from the pane.
     */
    private Line closestLine;

    public void removeClosestLine() {
        getChildren().remove(closestLine);
    }

    /**
     * Solve for the closest pair.
     */
    public void solve() {
        // only solve when points.size > 1 so that no unnecessary error
        // will be triggered (IllegalArgumentException in findClosest())
        if (points.size() > 1) {
            getChildren().remove(closestLine); // remove closestLine
            closest = findClosest(points); // find the closest

            // reset the position of closestLine
            closestLine = new Line(closest.p1.getX(), closest.p1.getY(),
                    closest.p2.getX(), closest.p2.getY());
            closestLine.setStroke(Color.DARKSLATEGRAY);
            closestLine.startXProperty().bind(closest.p1.xProperty());
            closestLine.startYProperty().bind(closest.p1.yProperty());
            closestLine.endXProperty().bind(closest.p2.xProperty());
            closestLine.endYProperty().bind(closest.p2.yProperty());

            getChildren().add(closestLine); // add it back to the pane
        }
    }

    /**
     * Provide an auto-solve mode for the user so that the solve
     * button doesn't have to hit every time a change is made.
     * A getter and a setter is provided as well.
     */
    private boolean autoSolve;

    public boolean isAutoSolve() {
        return autoSolve;
    }

    public void setAutoSolve(boolean autoSolve) {
        this.autoSolve = autoSolve;

        // solve immediately if auto-solve is being turned on
        if (autoSolve)
            solve();
    }

    /**
     * Find the closest pair of points using a divide-and-conquer
     * algorithm in chapter 22 of the book. This method is not to
     * be used outside of this class.
     *
     * @param points the list of points
     * @return the pair of closest points
     */
    private static Pair findClosest(List<Point> points) {
        // special conditions
        if (points.size() <= 1) // if the list does not have a pair
            throw new IllegalArgumentException("size: 1");
        else if (points.size() == 2) // if the list has only one pair, return it
            return new Pair(points.get(0), points.get(1));
        else if (points.size() == 3) { // if the list only has 3 points, return the closest
            // 3 pairs can be made from 3 points
            Pair p1 = new Pair(points.get(0), points.get(1));
            Pair p2 = new Pair(points.get(1), points.get(2));
            Pair p3 = new Pair(points.get(0), points.get(2));

            // return the closest pair from the three
            if (p1.distance() < p2.distance()) {
                if (p1.distance() < p3.distance())
                    return p1;
                return p3;
            } else if (p2.distance() < p3.distance())
                return p2;
            return p3;
        }

        /////////////////////////////////////////
        // Separate the list into two and find //
        // the closest points in both halves   //
        /////////////////////////////////////////

        // make a list with the points sorted in x-major order
        List<Point> pointsOrderedOnX = new ArrayList<>(points);
        Collections.sort(pointsOrderedOnX);

        // separate pointsOrderedOnX into two by the midpoint
        // (midpoint is the one in the middle index, not distance-wise)
        Point mid = pointsOrderedOnX.get((pointsOrderedOnX.size() - 1) / 2);
        List<Point> s1 = pointsOrderedOnX.subList(0, (pointsOrderedOnX.size() + 1) / 2);
        List<Point> s2 = pointsOrderedOnX.subList((pointsOrderedOnX.size() + 1) / 2, pointsOrderedOnX.size());

        // find the closest pairs in s1 and s2
        Pair p1 = findClosest(s1);
        double d1 = p1.distance();
        Pair p2 = findClosest(s2);
        double d2 = p2.distance();

        // compare the two closest pairs and get the closer one
        double d = (d1 < d2) ? d1 : d2;
        Pair closest = (d == d1) ? p1 : p2; // the current closest pair

        /////////////////////////////////////////////////////////////////////
        // Find the closest pair between a point in one half and a point   //
        // in the other half and compare it with the existing closest pair //
        /////////////////////////////////////////////////////////////////////

        // make a list with points ordered by y-values
        List<Point> pointsOrderedOnY = new ArrayList<>(points);
        pointsOrderedOnY.sort(Point.COMPARE_Y);

        List<Point> stripL = new ArrayList<>();
        List<Point> stripR = new ArrayList<>();
        for (Point p : pointsOrderedOnY) {
            if (s1.contains(p) && mid.getX() - p.getX() <= d)
                stripL.add(p);
            else if (s2.contains(p) && p.getX() - mid.getX() <= d)
                stripR.add(p);
        }

        int r = 0; // the index of a point in stripR
        for (Point p : stripL) {
            // skip the points in stripR below p.getHeadY() - d
            while (r < stripR.size() && stripR.get(r).getY() <= p.getY() - d)
                r++;

            int r1 = r;
            while (r1 < stripR.size() && Math.abs(stripR.get(r1).getY() - p.getY()) <= d) {
                // check if (p, q.get(r1)) is a possible closest pair
                double distance = distance(p, stripR.get(r1));
                if (distance < d) {
                    d = distance;
                    closest = new Pair(p, stripR.get(r1));
                }

                r1++;
            }
        }

        return closest;
    }

    // helper method for finding the closest pair
    private static double distance(Point p1, Point p2) {
        return new Pair(p1, p2).distance();
    }
}
