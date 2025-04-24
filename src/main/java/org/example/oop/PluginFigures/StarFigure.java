package org.example.oop.PluginFigures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;
import org.example.oop.Figures.Figure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarFigure extends Figure {
    @Override
    public Node getDrawable(double... params) throws IllegalArgumentException {
        final Polyline star = new Polyline();
        star.getPoints().addAll(Arrays.stream(params).boxed().toList());
        applyStyle(star);
        return star;
    }


    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        final int size = points.size();
        if (size < 2) {
            throw new IllegalArgumentException("At least two points are required to draw a star.");
        }

        final Point2D center = points.getFirst();
        double centerX = center.getX();
        double centerY = center.getY();

        final Point2D outerPoint = points.get(1);
        double outerRadius = center.distance(outerPoint);

        final List<Double> starPoints = getDoubles(outerRadius, centerX, centerY);

        final Polyline star = new Polyline();
        star.getPoints().addAll(starPoints);

        applyStyle(star);

        return star;
    }

    private static List<Double> getDoubles(final double outerRadius,
                                           final double centerX,
                                           final double centerY) {
        final double innerRadius = outerRadius * 0.5;

        final int spikes = 5;

        final List<Double> starPoints = new ArrayList<>();

        final double angleStep = Math.PI / spikes;

        final int size = spikes * 2 + 1;
        for (int i = 0; i < size; i++) {
            final double radius = (i % 2 == 0) ? outerRadius : innerRadius; // Чередуем внешний и внутренний радиус
            final double angle = i * angleStep;

            final double x = centerX + radius * Math.cos(angle);
            final double y = centerY + radius * Math.sin(angle);

            starPoints.add(x);
            starPoints.add(y);
        }
        return starPoints;
    }

    @Override
    public int getPointsCount() {
        return -1;
    }
}
