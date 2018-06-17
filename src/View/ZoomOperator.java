package View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class ZoomOperator {
    private Timeline timeline;

    public ZoomOperator() {
        this.timeline = new Timeline(60);
    }

    public void zoom(Node node1,Node node2,Node node3, double factor, double x, double y) {
        // determine scale
        double oldScale = node1.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds1 = node1.localToScene(node1.getLayoutBounds(),true );
        Bounds bounds2 = node2.localToScene(node2.getLayoutBounds(),true );
        Bounds bounds3 = node3.localToScene(node3.getLayoutBounds(),true );

        double dx1 = (x - (bounds1.getWidth() / 2 + bounds1.getMinX()));
        double dy1 = (y - (bounds1.getHeight() / 2 + bounds1.getMinY()));
        double dx2 = (x - (bounds2.getWidth() / 2 + bounds2.getMinX()));
        double dy2 = (y - (bounds2.getHeight() / 2 + bounds2.getMinY()));
        double dx3 = (x - (bounds3.getWidth() / 2 + bounds3.getMinX()));
        double dy3 = (y - (bounds3.getHeight() / 2 + bounds3.getMinY()));

        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(100), new KeyValue(node1.translateXProperty(), node1.getTranslateX() - f * dx1)),
                new KeyFrame(Duration.millis(100), new KeyValue(node1.translateYProperty(), node1.getTranslateY() - f * dy1)),
                new KeyFrame(Duration.millis(100), new KeyValue(node1.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(100), new KeyValue(node1.scaleYProperty(), scale)),

                new KeyFrame(Duration.millis(100), new KeyValue(node2.translateXProperty(), node2.getTranslateX() - f * dx2)),
                new KeyFrame(Duration.millis(100), new KeyValue(node2.translateYProperty(), node2.getTranslateY() - f * dy2)),
                new KeyFrame(Duration.millis(100), new KeyValue(node2.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(100), new KeyValue(node2.scaleYProperty(), scale)),

                new KeyFrame(Duration.millis(100), new KeyValue(node3.translateXProperty(), node3.getTranslateX() - f * dx3)),
                new KeyFrame(Duration.millis(100), new KeyValue(node3.translateYProperty(), node3.getTranslateY() - f * dy3)),
                new KeyFrame(Duration.millis(100), new KeyValue(node3.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(100), new KeyValue(node3.scaleYProperty(), scale))
        );
        timeline.play();
    }
}