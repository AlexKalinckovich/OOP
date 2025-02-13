package org.example.oop.Figures;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.List;

public class CircleFigure extends Figure{

    private final int CIRCLE_PARAMETERS_COUNT = 3;
    private final Circle originalCircle = new Circle();


    public CircleFigure() {
        drawable = originalCircle;
    }

    @Override
    protected Node createDrawableCopy() {
        final Circle circle = new Circle();
        circle.setRadius(originalCircle.getRadius());
        circle.setCenterX(originalCircle.getCenterX());
        circle.setCenterY(originalCircle.getCenterY());
        return circle;
    }

    @Override
    public void updateParameters(double[] parameters) {
        if(parameters.length != CIRCLE_PARAMETERS_COUNT) throw new IllegalArgumentException();
        originalCircle.setCenterX(parameters[0]);
        originalCircle.setCenterY(parameters[1]);
        originalCircle.setRadius(parameters[2]);
    }


    @Override
    public int getParameterCount() { return CIRCLE_PARAMETERS_COUNT; }

    @Override
    public String[] getParameterNames() {
        return new String[]{"Center X", "Center Y", "Radius"};
    }
}
