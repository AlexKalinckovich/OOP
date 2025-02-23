package org.example.oop.Models.DrawStrategy;

import javafx.geometry.Bounds;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.example.oop.Figures.Figure;
import org.example.oop.Models.PrintingClasses.MessageController;
import org.example.oop.Models.ValidationClasses.ParametersValidator;

public class CoordinateDrawStrategy implements DrawStrategy {
    private final ParametersValidator validator;
    private final MessageController messages;
    private final TextField[] fields;
    private final Pane drawingArea;

    public CoordinateDrawStrategy(
            final ParametersValidator validator,
            final MessageController messages,
            final TextField[] fields,
            final Pane drawingArea
    ) {
        this.validator = validator;
        this.messages = messages;
        this.fields = fields;
        this.drawingArea = drawingArea;
    }

    @Override
    public void draw(final Figure figure, final Bounds bounds) {

        validator.parseParameters(fields).ifPresent(params -> {
            if (validator.isCorrectCoordinates(params, bounds)) {
                try {
                    drawingArea.getChildren().add(figure.getDrawable(params));
                } catch (IllegalArgumentException e) {
                    messages.showAlert("Drawing Error", e.getMessage());
                }
            }
        });
    }
}