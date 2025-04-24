package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class RectangleFigure extends Figure{

    public RectangleFigure() {}

    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        final int MAX_PARAMETERS_COUNT = 4;
        if(params.length != MAX_PARAMETERS_COUNT) throw new IllegalArgumentException();
        final Polygon polygon = new Polygon();
        polygon.getPoints().setAll(
                params[0], params[1],
                params[0], params[2],
                params[2], params[3],
                params[2], params[1]
        );
        applyStyle(polygon);
        return polygon;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() != 2) throw new IllegalArgumentException("Need start and end points");
        final double x = Math.min(points.getFirst().getX(), points.get(1).getX());
        final double y = Math.min(points.getFirst().getY(), points.get(1).getY());
        final double width = Math.abs(points.getFirst().getX() - points.get(1).getX());
        final double height = Math.abs(points.getFirst().getY() - points.get(1).getY());
        final Rectangle rectangle = new Rectangle(x, y, width, height);
        applyStyle(rectangle);
        return rectangle;
    }

    @Override
    public int getPointsCount() {
        return -1;
    }
}
