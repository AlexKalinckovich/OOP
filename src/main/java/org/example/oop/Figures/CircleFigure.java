package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.List;

public class CircleFigure extends Figure {
    private final int CIRCLE_PARAMETERS_COUNT = 3;

    public CircleFigure(){}

    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        if(params.length != CIRCLE_PARAMETERS_COUNT) throw new IllegalArgumentException();
        this.params = params;
        final Circle circle = new Circle();
        applyStyle(circle);
        circle.setCenterX(params[0]);
        circle.setCenterY(params[1]);
        circle.setRadius(params[2]);
        return circle;
    }

    @Override
    public int getParameterCount() { return CIRCLE_PARAMETERS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Center X", "Center Y", "Radius"};
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() != 2) throw new IllegalArgumentException("Exactly 2 points required");
        final Point2D center = points.getFirst();
        final double radius = center.distance(points.get(1));
        return getDrawable(center.getX(), center.getY(), radius);
    }

    @Override
    public int getPointsCount() {
        return -1;
    }
}
