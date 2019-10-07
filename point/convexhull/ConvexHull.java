package point.convexhull;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Z on 10.20.
 * Introduction to Java Programming, 10th Edition
 * Chapter  22: Developing Efficient Algorithms
 *          22.10: Computational Geometry: Finding a Convex Hull
 *
 * This program provides a UI for plotting points and finding the
 * convex hull of the plotted points.
 */
public class ConvexHull extends Application {

    @Override
    public void start(Stage primaryStage) {
        ConvexHullPane pane = new ConvexHullPane();

        Button btSolve = new Button("Solve");
        Button btReset = new Button("Reset");

        pane.setOnMouseClicked(mouse -> pane.add(mouse.getX(), mouse.getY()));

        btSolve.setOnAction(event -> pane.solve());

        btReset.setOnAction(event -> pane.clear());

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btSolve, btReset);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(pane);
        root.setBottom(buttons);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Convex Hull");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
