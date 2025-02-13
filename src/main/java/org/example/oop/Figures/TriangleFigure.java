package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.List;

public class TriangleFigure extends Figure {

    private final int TRIANGLE_PARAMETERS_COUNT = 6;
    private final Polygon originalTriangle = new Polygon();
    public TriangleFigure() {
        drawable = originalTriangle;
    }

    @Override
    protected Node createDrawableCopy() {
        final Polygon polygon = new Polygon();
        polygon.getPoints().addAll(originalTriangle.getPoints());
        return polygon;
    }

    @Override
    public void updateParameters(final double[] parameters) {
        if(parameters.length != TRIANGLE_PARAMETERS_COUNT) throw new IllegalArgumentException();
        originalTriangle.getPoints().setAll(
                parameters[0],parameters[1],
                parameters[2],parameters[3],
                parameters[4],parameters[5]
        );
    }

    @Override
    public int getParameterCount() { return TRIANGLE_PARAMETERS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"X1", "Y1", "X2", "Y2", "X3", "Y3"};
    }
}
