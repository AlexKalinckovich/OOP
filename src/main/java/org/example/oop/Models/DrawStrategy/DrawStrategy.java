package org.example.oop.Models.DrawStrategy;

import javafx.geometry.Bounds;
import org.example.oop.Figures.Figure;

public interface DrawStrategy {
    void draw(Figure figure, Bounds drawingAreaBounds);
}