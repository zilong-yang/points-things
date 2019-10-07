package point;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Comparator;

/**
 * A class that represents an instance of a point on a plane,
 * consisting of an x- and an y-coordinate.
 */
public final class Point implements Comparable<Point> {

    /**
     * The x- and y-values of a Point.
     * The use of DoubleProperty makes the two fields compatible
     * when binding with JavaFX Properties.
     */
    protected final DoubleProperty x;
    protected final DoubleProperty y;

    /**
     * Constructs a point with the given x and y values.
     *
     * @param x the x-value
     * @param y the y-value
     */
    public Point(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }

    /////////////////////////
    // getters and setters //
    /////////////////////////

    public double getX()                { return x.get(); }
    public double getY()                { return y.get(); }
    public void setX(double x)          { this.x.set(x); }
    public void setY(double y)          { this.y.set(y); }
    public DoubleProperty xProperty()   { return x; }
    public DoubleProperty yProperty()   { return y; }

    /**
     * Returns the distance of this point and the given point.
     *
     * @return the distance
     */
    public double distance(Point other) {
        double a = this.getX() - other.getX();
        double b = this.getY() - other.getY();
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Compares this point with another object. The object is
     * considered to be equal to this point if and only if it is
     * also a point and it has the exact same x and y values.
     *
     * @param obj the object
     * @return  true if obj is equal to this point;
     *          false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point other = ((Point) obj);
            return (this.x.get() == other.x.get() &&
                    this.y.get() == other.y.get());
        }
        return false;
    }

    /**
     * Returns a string representation of this point. The string
     * consists of the x- and y-coordinates.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return String.format("(%.0f, %.0f)", x.get(), y.get());
    }

    /**
     * Compares the point o with this point by their x-values.
     * If they have the same values, compare their y-values.
     *
     * @param o the point
     * @return  -1 if this point is smaller than o;
     *          1 if this point is greater than o;
     *          0 if they are equal
     */
    @Override
    public int compareTo(Point o) {
        if (this.x.get() < o.x.get())
            return -1;
        else if (this.x.get() > o.x.get())
            return 1;
        else
            return COMPARE_Y.compare(this, o);
    }

    /**
     * A comparator class for y-major comparisons.
     */
    public static final Comparator<Point> COMPARE_Y = (p1, p2) -> {
        // compares two points by their y-values
        if (p1.y.get() < p2.y.get())
            return -1;
        else if (p1.y.get() > p2.y.get())
            return 1;
        return 0;
    };
}
