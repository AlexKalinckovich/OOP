package org.example.oop.Models.DrawStrategy;

import javafx.geometry.Bounds;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.example.oop.Figures.Figure;
import org.example.oop.Models.PrintingClasses.MessageController;
import org.example.oop.Models.ValidationClasses.ParametersValidator;

public class CoordinateDrawStrategy implements DrawStrategy {
    private final TextField[] fields;
    private final Pane drawingArea;

    public CoordinateDrawStrategy(
            final TextField[] fields,
            final Pane drawingArea
    ) {
        this.fields = fields;
        this.drawingArea = drawingArea;
    }

    @Override
    public void draw(final Figure figure, final Bounds bounds) {

        ParametersValidator.parseParameters(fields).ifPresent(params -> {
            if (ParametersValidator.isCorrectCoordinates(params, bounds)) {
                try {
                    drawingArea.getChildren().add(figure.getDrawable(params));
                } catch (IllegalArgumentException e) {
                    MessageController.showAlert("Drawing Error", e.getMessage());
                }
            }
        });
    }
}