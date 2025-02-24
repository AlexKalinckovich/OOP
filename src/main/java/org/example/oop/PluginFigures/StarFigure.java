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
    public int getParameterCount() {return 10;}


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
    private static List<Double> getDoubles(double outerRadius, double centerX, double centerY) {
        double innerRadius = outerRadius * 0.5;

        // Количество лучей звезды
        final int spikes = 5;

        // Список координат для вершин звезды
        List<Double> starPoints = new ArrayList<>();

        // Угол между соседними вершинами
        double angleStep = Math.PI / spikes;

        // Рассчитываем координаты вершин
        for (int i = 0; i < spikes * 2; i++) {
            double radius = (i % 2 == 0) ? outerRadius : innerRadius; // Чередуем внешний и внутренний радиус
            double angle = i * angleStep;

            // Координаты вершины
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

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
