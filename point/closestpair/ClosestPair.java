package point.closestpair;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import point.Point;

/**
 * Created by Z on 08.26.
 * Introduction to Java Programming, 10th Edition
 * Chapter  22: Developing Efficient Algorithms
 *          22.8: Finding the Closest Pair of Points Using Divide-and-Conquer
 * <p>
 * This program finds the closest pair of points on a plane using
 * a divide-and-conquer algorithm. A JavaFX UI is provided to allow
 * the user to plot, to see the closest pair visually, etc.
 */
public class ClosestPair extends Application {

    @Override
    public void start(Stage primaryStage) {
        ///////////////////////////// Nodes /////////////////////////////

        // the pane for plotting
        ClosestPairPane pane = new ClosestPairPane();

        // a list view of points on the pane
        ListView<Point> list = new ListView<>(pane.getPoints());

        // user optional controls
        CheckBox ckbAxes = new CheckBox("Axes");
        CheckBox ckbAutoSolve = new CheckBox("Auto-solve");

        // closest pair information
        TextField tfClosest = new TextField();
        TextField tfDistance = new TextField();

        // user control
        Button btSolve = new Button("Solve");
        Button btClear = new Button("Clear");
        Button btPrint = new Button("Print");

        ///////////////////////////// Properties /////////////////////////////

        pane.setPadding(new Insets(10));

        list.setStyle("-fx-font-size: 14");
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        HBox top = new HBox(5);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(0, 5, 5, 5));
        top.getChildren().addAll(ckbAxes, ckbAutoSolve);

        tfClosest.setEditable(false);
        tfDistance.setEditable(false);

        GridPane closestInfo = new GridPane();
        closestInfo.setPadding(new Insets(5, 0, 0, 0));
        closestInfo.setAlignment(Pos.CENTER);
        closestInfo.setHgap(5);
        closestInfo.setVgap(5);
        closestInfo.addRow(0, new Label("Closest Pair:"), tfClosest);
        closestInfo.addRow(1, new Label("Distance:"), tfDistance);
        btSolve.setDefaultButton(true);
        btClear.setCancelButton(true);

        HBox checkBoxes = new HBox(5);
        checkBoxes.setAlignment(Pos.CENTER);
        checkBoxes.getChildren().addAll(ckbAxes, ckbAutoSolve);

        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 0, 0));
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btSolve, btClear, btPrint);

        VBox bottom = new VBox(5);
        bottom.setPadding(new Insets(10, 5, 0, 5));
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(checkBoxes, closestInfo, buttons);

        ScrollPane scrollPane = new ScrollPane(list);

        VBox left = new VBox(5);
        left.getChildren().addAll(scrollPane, bottom);

        Pane leftPane = new Pane(left);
        leftPane.setPadding(new Insets(0, 5, 0, 0));

        ///////////////////////////// Events /////////////////////////////

        // when the pane is clicked, trigger the following
        pane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // add a point located at the position of the mouse
                pane.add(event.getX(), event.getY());

                // create a visual circle for the point and set its events
                Circle c = pane.getCircles().get(pane.numOfPoints() - 1);
                c.setOnMouseClicked(mouse -> {
                    // remove if the circle is right-clicked
                    if (mouse.getButton() == MouseButton.SECONDARY)
                        pane.remove(pane.getCircles().indexOf(c));
                });

                // When the circle is dragged, move to wherever it's dragged to and,
                // if auto-solve is on, solve for the closest pair as the circle is
                // dragged in case the position of the pair or the pair itself has
                // changed during dragging.
                c.setOnMouseDragged(mouse -> {
                    if (mouse.getX() <= pane.getWidth() - 5 && mouse.getX() >= 5)
                        c.setCenterX(mouse.getX());
                    if (mouse.getY() <= pane.getHeight() - 5 && mouse.getY() >= 5)
                        c.setCenterY(mouse.getY());

                    if (pane.isAutoSolve())
                        pane.solve();
                    else
                        pane.removeClosestLine();
                });

                // When the mouse enters the circle, change the border of the circle
                // to black and the color to sky blue, and select the interacting
                // circle in the list view.
                c.setOnMouseEntered(mouse -> {
                    c.setStroke(Color.BLACK);
                    c.setFill(Color.SKYBLUE);

                    int index = pane.getCircles().indexOf(c);
                    list.getSelectionModel().select(pane.getPoints().get(index));
                    list.scrollTo(index);
                });

                // When the mouse is not in the circle, set the border of the circle
                // to white and the color to dark slate gray, and clear all selections
                // of the list view.
                c.setOnMouseExited(mouse -> {
                    c.setStroke(Color.WHITE);
                    c.setFill(Color.DARKSLATEGRAY);
                    list.getSelectionModel().clearSelection();
                });


                if (pane.isAutoSolve() && pane.getClosest() != null) {
                    tfClosest.setText(pane.getClosest().toString());
                    tfDistance.setText(String.format("%.2f", pane.getClosest().distance()));
                }
            }
        });

        // when enter is pressed, solve for the closest pair
        // when esc is pressed, clear all the points
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btSolve.fire();
            if (event.getCode() == KeyCode.ESCAPE)
                btClear.fire();
        });

        // when the mouse is dragged, refresh the list view to update the positions
        // and update the closest pair information
        pane.setOnMouseDragged(event -> {
            list.refresh();
            if (pane.isAutoSolve() && pane.getClosest() != null) {
                tfClosest.setText(pane.getClosest().toString());
                tfDistance.setText(String.format("%.2f", pane.getClosest().distance()));
            }
        });

        // when the axes check box is selected/unselected, change the visibility
        // to the corresponding condition.
        ckbAxes.setOnAction(event -> pane.setAxesVisible(ckbAxes.isSelected()));

        // when the auto-solve check box is selected/unselected, change the auto-solve
        // mode to the corresponding condition.
        ckbAutoSolve.setOnAction(event -> {
            pane.setAutoSolve(ckbAutoSolve.isSelected());
            btSolve.fire();
        });

        // when the solve button is clicked, solve for the closest pair and update
        // the information of the closest pair
        btSolve.setOnAction(event -> {
            pane.solve();
            if (pane.getClosest() != null) {
                tfClosest.setText(pane.getClosest().toString());
                tfDistance.setText(String.format("%.2f", pane.getClosest().distance()));
            }
        });

        // when the clear button is clicked, remove all points on the pane and clear
        // the closest pair information
        btClear.setOnAction(event -> {
            pane.clear();
            tfClosest.setText("");
            tfDistance.setText("");
        });

        // when the print is clicked, print all the existing points on the pane in
        // console.
        btPrint.setOnAction(event -> System.out.println(pane.getPoints()));

        ///////////////////////////// Root /////////////////////////////

        BorderPane root = new BorderPane(pane);
        root.setPadding(new Insets(10));
        root.setLeft(leftPane);

        Scene scene = new Scene(root, 800, 555);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Closest Pair");
        primaryStage.show();

        pane.requestFocus();
    }
}
