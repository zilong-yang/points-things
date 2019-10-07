package point;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Z on 09.05.
 * Introduction to Java Programming, 10th Edition
 * Chapter  22: Developing Efficient Algorithms
 *
 * This class represents an instance of a plotter plane, that is,
 * a visual pane on screen that allows some functions for the
 * user to use for plotting points. This extends the Pane class
 * in JavaFX.
 *
 * The pane keeps track of the points that the user plotted and
 * their representation in circles, which makes them more realistic
 * to points on a graph. It provides some basic functions for
 * plotting such as adding and removing points.
 */
public class PlotPane extends Pane {

    // a list of points plotted
    protected ObservableList<Point> points;

    // a list of circles that represent the points on the plane
    protected ObservableList<Circle> circles;

    // the number of points present on the pane
    private int numPoints;

    // the x- and y-axis
    private Line xAxis;
    private Line yAxis;
    private boolean axesVisible;

    /**
     * Constructs a default PlotPane with the size of 350x350.
     */
    public PlotPane() {
        this(350, 350);
    }

    /**
     * Constructs a PlotPane with the specified width and height
     * of this pane.
     *
     * @param width  the width
     * @param height the height
     */
    public PlotPane(double width, double height) {
        points = FXCollections.observableArrayList();
        circles = FXCollections.observableArrayList();

        // customization for the pane
        setPrefWidth(width);
        setPrefHeight(height);
        setStyle("-fx-border-color: lightgrey;" +
                "-fx-border-width: 4px;" +
                "-fx-background-color: lightgoldenrodyellow;" +
                "-fx-border-radius: 2%;" +
                "-fx-background-radius: 1.5%");

        // draw the axes
        xAxis = new Line(4, height / 2, width - 4, height / 2);
        yAxis = new Line(width / 2, 4, width / 2, height - 4);

        xAxis.setStroke(Color.GREY);
        yAxis.setStroke(Color.GREY);
        xAxis.setOpacity(0);
        yAxis.setOpacity(0);

        xAxis.startYProperty().bind(heightProperty().divide(2));
        xAxis.endXProperty().bind(widthProperty().subtract(4));
        xAxis.endYProperty().bind(heightProperty().divide(2));
        yAxis.startXProperty().bind(widthProperty().divide(2));
        yAxis.endXProperty().bind(widthProperty().divide(2));
        yAxis.endYProperty().bind(heightProperty().subtract(4));

        getChildren().addAll(xAxis, yAxis);
    }

    /**
     * Returns an unmodifiable list of the points on this pane.
     *
     * @return an unmodifiable version of points.
     */
    public ObservableList<Point> getPoints() {
        return FXCollections.unmodifiableObservableList(points);
    }

    /**
     * Returns unmodifiable list of the circles on this pane.
     *
     * @return an unmodifiable version of circles
     */
    public ObservableList<Circle> getCircles() {
        return FXCollections.unmodifiableObservableList(circles);
    }

    /**
     * Returns the number of points.
     *
     * @return number of points
     */
    public int numOfPoints() {
        return numPoints;
    }

    /**
     * Getter and setter methods for axesVisible.
     */
    public boolean isAxesVisible() {
        return axesVisible;
    }

    public void setAxesVisible(boolean axesVisible) {
        if (axesVisible ^ this.axesVisible) {
            this.axesVisible = axesVisible;
            updateAxes();
        }
    }

    /**
     * Update the axes for changes in axesVisible.
     */
    private void updateAxes() {
        // hides/shows the axes by changing their opacity
        int opacity = axesVisible ? 1 : 0;
        xAxis.setOpacity(opacity);
        yAxis.setOpacity(opacity);
    }

    /**
     * Add a point to this pane with the given x- and y-coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void add(double x, double y) {
        add(new Point(x, y));
    }

    /**
     * Add the given point to this pane as well as creating a
     * circle to represent this point on the pane.
     *
     * @param p the point to add
     */

    public void add(Point p) {
        // if the pane doesn't already have this point
        // (necessary to check because dragging a circle also
        //  triggers the dragging event of the pane, resulting in
        //  adding a new point on the released position)
        if (!points.contains(p)) {
            points.add(p);
            numPoints++;

            // create a circle on the mouse position
            Circle c = new Circle(p.getX(), p.getY(), 5);
            c.setFill(Color.DARKSLATEGRAY);

            // bidirectionally bind the coordinates of the point and the
            // position of its circle representation, so that the change
            // in one will as well affect the other
            p.xProperty().bindBidirectional(c.centerXProperty());
            p.yProperty().bindBidirectional(c.centerYProperty());

            circles.add(c);
            getChildren().add(c);
        }
    }

    /**
     * Remove the point of the specified index from this pane.
     *
     * @param index the index in the list
     */
    public void remove(int index) {
        if (index >= 0) {
            numPoints--;

            // remove from both lists and the pane
            points.remove(index);
            getChildren().remove(circles.remove(index));
        }
    }

    /**
     * Removes the given point from this pane.
     *
     * @param p the point
     */
    public void remove(Point p) {
        // find the index of p in points and remove
        remove(points.indexOf(p));
    }

    /**
     * Remove all points from this pane as well as any nodes on
     * the pane except the axes.
     */
    public void clear() {
        numPoints = 0;
        points.clear();
        circles.clear();
        getChildren().clear();
        getChildren().addAll(xAxis, yAxis);
    }
}
