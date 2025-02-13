package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Ellipse;

import java.util.List;

public class EllipseFigure extends Figure {
    private final Ellipse originalEllipse = new Ellipse();
    private final int MAX_ELLIPSE_COUNT = 4;

    public EllipseFigure() {
        drawable = originalEllipse;
    }

    @Override
    protected Node createDrawableCopy() {
        final Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(originalEllipse.getCenterX());
        ellipse.setCenterY(originalEllipse.getCenterY());
        ellipse.setRadiusX(originalEllipse.getRadiusX());
        ellipse.setRadiusY(originalEllipse.getRadiusY());
        return ellipse;
    }

    @Override
    public void updateParameters(final double[] parameters) {
        if(parameters.length != MAX_ELLIPSE_COUNT) throw new IllegalArgumentException();
        originalEllipse.setCenterX(parameters[0]);
        originalEllipse.setCenterY(parameters[1]);
        originalEllipse.setRadiusX(parameters[2]);
        originalEllipse.setRadiusY(parameters[3]);
    }

    @Override
    public int getParameterCount() { return MAX_ELLIPSE_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Center X", "Center Y", "Radius X", "Radius Y"};
    }
}