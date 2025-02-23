package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;
import org.example.oop.FigureInterfaces.MouseDrawable;

import java.util.List;
import java.util.stream.DoubleStream;

public class PolylineFigure extends Figure implements MouseDrawable {
    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        final Polyline polyline = new Polyline();
        polyline.getPoints().setAll(
                DoubleStream.of(params)
                        .boxed()
                        .toList()
        );
        applyStyle(polyline);
        return polyline;
    }

    @Override
    public int getParameterCount() { return 1; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Points (x1 y1 x2 y2...)"};
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.CLICKS;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() < 2) throw new IllegalArgumentException("At least 2 points required");
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
