package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import org.example.oop.FigureInterfaces.MouseDrawable;

import java.util.List;

public class EllipseFigure extends Figure implements MouseDrawable {
    private final int MAX_ELLIPSE_COUNT = 4;

    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        if(params.length != MAX_ELLIPSE_COUNT) throw new IllegalArgumentException();
        this.params = params;
        final Ellipse ellipse = new Ellipse();
        applyStyle(ellipse);
        ellipse.setCenterX(params[0]);
        ellipse.setCenterY(params[1]);
        ellipse.setRadiusX(params[2]);
        ellipse.setRadiusY(params[3]);
        return ellipse;
    }

    @Override
    public int getParameterCount() { return MAX_ELLIPSE_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Center X", "Center Y", "Radius X", "Radius Y"};
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() != 2) {
            throw new IllegalArgumentException("Ellipse requires exactly 2 points (start and end)");
        }
        final Point2D start = points.getFirst();
        final Point2D end = points.get(1);

        final double centerX = (start.getX() + end.getX()) / 2;
        final double centerY = (start.getY() + end.getY()) / 2;
        final double radiusX = Math.abs(start.getX() - end.getX()) / 2;
        final double radiusY = Math.abs(start.getY() - end.getY()) / 2;

        return getDrawable(centerX, centerY, radiusX, radiusY);
    }

    @Override
    public int getPointsCount() {
        return -1;
    }
}