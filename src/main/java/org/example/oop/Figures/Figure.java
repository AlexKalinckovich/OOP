package org.example.oop.Figures;

import javafx.scene.Node;

import java.util.List;

public abstract class Figure {
    protected Node drawable;

    public Node getDrawable() {
        return createDrawableCopy();
    }

    protected abstract Node createDrawableCopy();
    public abstract void updateParameters(double[] params) throws IllegalArgumentException,NumberFormatException;
    public abstract int getParameterCount();
    public abstract String[] getParameterNames();
}

