package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

public class PolygonFigure extends Figure {
    @Override
    public Node getDrawable(double... params) throws IllegalArgumentException {
        final List<Double> list = DoubleStream.of(params)
                .collect(
                        LinkedList::new,
                        LinkedList::addLast,
                        LinkedList::addAll
                );
        list.addLast(list.getFirst());
        list.addLast(list.get(1));
        final Polyline polyline = new Polyline();
        polyline.getPoints().setAll(list);
        applyStyle(polyline);
        return polyline;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.CLICKS;
    }

    @Override
    public Node createFromMousePoints(List<Point2D> points) throws IllegalArgumentException {
        double[] params = points.stream()
                .flatMapToDouble(p -> DoubleStream.of(p.getX(), p.getY()))
                .toArray();

        return getDrawable(params);
    }

    @Override
    public int getPointsCount() {
        return Integer.MAX_VALUE;
    }
}
