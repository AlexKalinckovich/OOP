package org.example.oop.Models.ValidationClasses;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.example.oop.Figures.CircleFigure;
import org.example.oop.Figures.Figure;
import org.example.oop.Figures.TriangleFigure;

import java.util.List;

public class ParametersValidator {

    public ParametersValidator() {
    }

    public static boolean isPointOutOfBounds(double x, double y, final Bounds bounds) {
        return  x <= bounds.getMinX() || x >= bounds.getMaxX() ||
                y <= bounds.getMinY() || y >= bounds.getMaxY();
    }


    public static boolean isFigureInBounds(final List<Point2D> points, final Bounds bounds, final Figure figure) {
        boolean result = true;
        if (figure instanceof CircleFigure) {
            final Point2D first = points.getFirst();
            final double centerX = first.getX();
            final double centerY = first.getY();
            final double radius = points.get(1).distance(first);

            result = (centerX - radius >= bounds.getMinX()) &&
                     (centerX + radius <= bounds.getMaxX()) &&
                     (centerY - radius >= bounds.getMinY()) &&
                     (centerY + radius <= bounds.getMaxY());
        }else if(figure instanceof TriangleFigure) {

            final Point2D start = points.getFirst();
            final Point2D end = points.get(1);

            final double centerX = start.getX();
            final double centerY = start.getY();
            final double width = Math.abs(start.getX() - end.getX());
            final double height = Math.abs(start.getY() - end.getY());
            result = centerX - width / 2  >=  bounds.getMinX()  &&
                     centerX + width / 2  <=  bounds.getMaxX()  &&
                     centerY - height / 2 >= bounds.getMinY()   &&
                     centerY + height / 2 <= bounds.getMaxY();
        }

        return result;
    }
}
