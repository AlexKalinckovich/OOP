package org.example.oop.Figures;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.List;

public class LineFigure extends Figure{

    public LineFigure() {}

    @Override
    public Node getDrawable(final double... params) throws IllegalArgumentException {
        final int LINE_PARAMS_COUNT = 4;
        if(params.length != LINE_PARAMS_COUNT) throw new IllegalArgumentException();
        final Line line = new Line();
        applyStyle(line);
        line.setStartX(params[0]);
        line.setStartY(params[1]);
        line.setEndX(params[2]);
        line.setEndY(params[3]);
        return line;
    }


    @Override
    public InteractionType getInteractionType() {
        return InteractionType.CLICKS;
    }

    @Override
    public Node createFromMousePoints(final List<Point2D> points) throws IllegalArgumentException {
        if (points.size() != 2) throw new IllegalArgumentException("Need exactly 2 points");
        return new Line(points.getFirst().getX(), points.getFirst().getY(),
                        points.get(1).getX(), points.get(1).getY());
    }

    @Override
    public int getPointsCount() {
        return 2;
    }
}