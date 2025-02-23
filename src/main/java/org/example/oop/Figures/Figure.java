package org.example.oop.Figures;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.example.oop.FigureInterfaces.Drawable;
import org.example.oop.FigureInterfaces.MouseDrawable;
import org.example.oop.Models.Settings.FigureSettings;

import java.io.Serializable;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CircleFigure.class, name = "circle"),
        @JsonSubTypes.Type(value = RectangleFigure.class, name = "rectangle"),
        @JsonSubTypes.Type(value = TriangleFigure.class, name = "triangle"),
        @JsonSubTypes.Type(value = EllipseFigure.class, name = "ellipse"),
        @JsonSubTypes.Type(value = LineFigure.class, name = "line"),
        @JsonSubTypes.Type(value = PolylineFigure.class, name = "polyline")
})
public abstract class Figure extends Node implements Serializable {
    private FigureSettings settings = new FigureSettings();

    protected double[] params = new double[0];

    protected void applyStyle(Node node) {
        if (node instanceof Shape shape) {
            shape.setFill(settings.getFillColor());
            shape.setStroke(settings.getStrokeColor());
            shape.setStrokeWidth(settings.getStrokeWidth());
            if (settings.getDashPattern() != null) {
                shape.getStrokeDashArray().setAll(settings.getDashPattern());
            }
        }
    }

    public FigureSettings getSettings() { return settings; }
    public void setSettings(FigureSettings settings) { this.settings = settings; }

    public abstract Node getDrawable(double... params) throws IllegalArgumentException;
    public abstract int getParameterCount();
    public abstract String[] getParameterNames();
    public double[] getParams() { return params; }
}