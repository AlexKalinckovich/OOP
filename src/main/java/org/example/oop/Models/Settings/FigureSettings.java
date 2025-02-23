package org.example.oop.Models.Settings;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.List;

public class FigureSettings {
    private Paint fillColor = Color.TRANSPARENT;
    private Paint strokeColor = Color.BLACK;
    private double strokeWidth = 1.0;
    private List<Double> dashPattern = null;

    public Paint getFillColor() { return fillColor; }
    public void setFillColor(Paint fillColor) { this.fillColor = fillColor; }

    public Paint getStrokeColor() { return strokeColor; }
    public void setStrokeColor(Paint strokeColor) { this.strokeColor = strokeColor; }

    public double getStrokeWidth() { return strokeWidth; }
    public void setStrokeWidth(double strokeWidth) { this.strokeWidth = strokeWidth; }

    public List<Double> getDashPattern() { return dashPattern; }
    public void setDashPattern(List<Double> dashPattern) { this.dashPattern = dashPattern; }
}