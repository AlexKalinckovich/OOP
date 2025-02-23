package org.example.oop.Models.Factories;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.example.oop.Models.DrawStrategy.CoordinateDrawStrategy;
import org.example.oop.Models.DrawStrategy.DrawStrategy;
import org.example.oop.Models.DrawStrategy.MouseDrawStrategy;
import org.example.oop.Models.PrintingClasses.MessageController;
import org.example.oop.Models.ValidationClasses.ParametersValidator;

import java.util.Stack;

public class StrategyFactory {
    public enum StrategyType {COORDINATE,MOUSE}
    public static DrawStrategy createStrategy(
            final StrategyType type,
            final ParametersValidator validator,
            final MessageController messages,
            final TextField[] fields,
            final Pane drawingArea,
            final Stack<Node> drawingHistory
    ) {
        return switch (type) {
            case StrategyType.COORDINATE -> new CoordinateDrawStrategy(validator, messages, fields, drawingArea);
            case StrategyType.MOUSE -> new MouseDrawStrategy(validator,messages, drawingArea,drawingHistory);
        };
    }

}
