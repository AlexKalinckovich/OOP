package org.example.oop.FigureInterfaces;

import javafx.geometry.Point2D;
import javafx.scene.Node;


import java.util.List;

public interface MouseDrawable {
    enum InteractionType { DRAG, CLICKS }
    InteractionType getInteractionType();
    Node createFromMousePoints(List<Point2D> points) throws IllegalArgumentException;
    int getPointsCount(); // сколько точек надо для того, чтобы нарисовать фигуру
}