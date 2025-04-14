package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.List;

public class TriangleFigure extends Figure {
    private final int TRIANGLE_PARAMETERS_COUNT = 4;

    public TriangleFigure() {}

    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        if (params.length != TRIANGLE_PARAMETERS_COUNT) {
            throw new IllegalArgumentException("Triangle requires 4 parameters: centerX, centerY, width, height");
        }
        final double centerX = params[0];
        final double centerY = params[1];
        final double width = params[2];
        final double height = params[3];

        final Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                centerX, centerY - height / 2,
                centerX - width / 2, centerY + height / 2,
                centerX + width / 2, centerY + height / 2
        );
        applyStyle(triangle);
        return triangle;
    }

    @Override
    public int getParameterCount() { return TRIANGLE_PARAMETERS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"X1", "Y1", "X2", "Y2", "X3", "Y3"};
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() != 2) {
            throw new IllegalArgumentException("Triangle requires exactly 2 points (start and end)");
        }
        final Point2D start = points.getFirst();
        final Point2D end = points.get(1);

        final double centerX = start.getX();
        final double centerY = start.getY();
        final double width = Math.abs(start.getX() - end.getX());
        final double height = Math.abs(start.getY() - end.getY());

        return getDrawable(centerX, centerY, width, height);
    }

    @Override
    public int getPointsCount() {
        return -1;
    }
}
