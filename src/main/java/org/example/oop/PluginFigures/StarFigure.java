package org.example.oop.PluginFigures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;
import org.example.oop.Figures.Figure;
import org.jetbrains.annotations.NotNull;

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
    public int getParameterCount() {return 1;}


    @Override
    public String[] getParameterNames() {
        return new String[]{"x", "y..."};
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.DRAG;
    }

    @Override
    public Node createFromMousePoints(List<Point2D> points) throws IllegalArgumentException {
        final int size = points.size();
        if (size < 2) {
            throw new IllegalArgumentException("At least two points are required to draw a star.");
        }

        // Получаем центр звезды (первая точка)
        Point2D center = points.getFirst();
        double centerX = center.getX();
        double centerY = center.getY();

        // Рассчитываем внешний радиус (расстояние до второй точки)
        Point2D outerPoint = points.get(1);
        double outerRadius = center.distance(outerPoint);

        // Внутренний радиус (например, половина внешнего радиуса)
        List<Double> starPoints = getDoubles(outerRadius, centerX, centerY);

        // Создаём Polyline и добавляем рассчитанные точки
        final Polyline star = new Polyline();
        star.getPoints().addAll(starPoints);

        // Применяем стиль (если требуется)
        applyStyle(star);

        return star;
    }

    @NotNull
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
