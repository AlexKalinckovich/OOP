package org.example.oop.Models.Factories;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.example.oop.Models.DrawStrategy.CoordinateDrawStrategy;
import org.example.oop.Models.DrawStrategy.DrawStrategy;
import org.example.oop.Models.DrawStrategy.MouseDrawStrategy;

import java.util.Stack;

public class StrategyFactory {
    public enum StrategyType {COORDINATE,MOUSE}
    public static DrawStrategy createStrategy(
            final StrategyType type,
            final TextField[] fields,
            final Pane drawingArea,
            final Stack<Node> drawingHistory,
            final Stack<Node> redoHistory
    ) {
        return switch (type) {
            case StrategyType.COORDINATE -> new CoordinateDrawStrategy(fields, drawingArea);
            case StrategyType.MOUSE -> new MouseDrawStrategy(drawingArea,drawingHistory,redoHistory);
        };
    }

}
