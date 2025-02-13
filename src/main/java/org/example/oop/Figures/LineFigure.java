package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.List;

public class LineFigure extends Figure {
    private final int LINE_PARAMS_COUNT = 4;
    private final Line originalLine = new Line();
    public LineFigure() {
        drawable = originalLine;
    }

    @Override
    protected Node createDrawableCopy() {
        final Line newLine = new Line();
        newLine.setStartX(originalLine.getStartX());
        newLine.setStartY(originalLine.getStartY());
        newLine.setEndX(originalLine.getEndX());
        newLine.setEndY(originalLine.getEndY());
        return newLine;
    }

    @Override
    public void updateParameters(final double[] parameters) {
        if(parameters.length != LINE_PARAMS_COUNT) throw new IllegalArgumentException();
        originalLine.setStartX(parameters[0]);
        originalLine.setStartY(parameters[1]);
        originalLine.setEndX(parameters[2]);
        originalLine.setEndY(parameters[3]);
    }

    @Override
    public int getParameterCount() { return LINE_PARAMS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Start X", "Start Y", "End X", "End Y"};
    }
}