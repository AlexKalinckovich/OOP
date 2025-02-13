package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Polyline;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PolylineFigure extends Figure {
    private final Polyline originalPolyline = new Polyline();
    private final int MAX_PARAMETERS_COUNT = 10;
    private final int SINGLE_FIELD = 1;
    public PolylineFigure() {
        drawable = originalPolyline;
    }

    @Override
    protected Node createDrawableCopy() {
        final Polyline newPolyline = new Polyline();
        newPolyline.getPoints().addAll(originalPolyline.getPoints());
        return newPolyline;
    }

    @Override
    public void updateParameters(final double[] parameters) {
        if(parameters.length > MAX_PARAMETERS_COUNT) throw new IllegalArgumentException();
        final List<Double> list = Arrays.stream(parameters)
                .collect(LinkedList::new,
                        LinkedList::addLast,
                        LinkedList::addAll);
        originalPolyline.getPoints().setAll(list);
    }

    @Override
    public int getParameterCount() { return SINGLE_FIELD; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Points (x1 y1 x2 y2...)"};
    }
}
