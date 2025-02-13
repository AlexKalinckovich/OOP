package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.List;

public class RectangleFigure extends Figure {
    private final int MAX_PARAMETERS_COUNT = 4;
    private final Polygon originalPolygon = new Polygon();
    public RectangleFigure() {
        drawable = originalPolygon;
    }

    @Override
    protected Node createDrawableCopy() {
        final Polygon polygon = new Polygon();
        polygon.getPoints().addAll(originalPolygon.getPoints());
        return polygon;
    }

    @Override
    public void updateParameters(final double[] parameters) {
        if(parameters.length != MAX_PARAMETERS_COUNT) throw new IllegalArgumentException();
        originalPolygon.getPoints().setAll(
                parameters[0], parameters[1],
                parameters[0], parameters[3],
                parameters[2], parameters[3],
                parameters[2], parameters[1]
        );
    }

    @Override
    public int getParameterCount() { return MAX_PARAMETERS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Left", "Top", "Right", "Bottom"};
    }
}
