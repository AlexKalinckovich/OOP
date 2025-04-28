package org.example.oop.Models.DrawStrategy;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.example.oop.Figures.Figure;
import org.example.oop.FigureInterfaces.MouseDrawable;
import org.example.oop.Models.PrintingClasses.MessageController;
import org.example.oop.Models.ValidationClasses.ParametersValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MouseDrawStrategy implements DrawStrategy {
    private final Pane drawingArea;
    private final List<Point2D> points = new ArrayList<>();
    private final List<Circle> tempDots = new ArrayList<>();
    private final Stack<Node> drawingHistory = new Stack<>();
    private final Stack<Node> redoHistory = new Stack<>();
    private Node previewNode;

    public MouseDrawStrategy(final Pane drawingArea) {
        this.drawingArea = drawingArea;
    }

    @Override
    public void draw(final Figure figure, final Bounds bounds) {
        setupHandlers(figure);
    }

    private void setupHandlers(final Figure figure) {
        drawingArea.setOnMousePressed(null);
        drawingArea.setOnMouseDragged(null);
        drawingArea.setOnMouseReleased(null);
        drawingArea.setOnMouseClicked(null);

        if (figure.getInteractionType() == MouseDrawable.InteractionType.DRAG) {
            setupDragHandlers(figure);
        } else {
            setupClickHandlers(figure);
        }
    }

    private void setupDragHandlers(final Figure figure) {
        final Point2D[] startPoint = new Point2D[1];

        drawingArea.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Валидация начальной точки
                if (ParametersValidator.isPointOutOfBounds(event.getX(), event.getY(), drawingArea.getLayoutBounds())) {
                    return;
                }

                startPoint[0] = new Point2D(event.getX(), event.getY());
                points.clear();
                points.add(startPoint[0]);
                updatePreview(figure);
            }
        });

        drawingArea.setOnMouseDragged(event -> {
            if (startPoint[0] != null) {
                if (ParametersValidator.isPointOutOfBounds(event.getX(), event.getY(), drawingArea.getLayoutBounds())) {
                    return;
                }

                final Point2D current = new Point2D(event.getX(), event.getY());
                if (points.size() > 1) {
                    points.set(1, current);
                }
                else {
                    points.add(current);
                }
                updatePreview(figure);
            }
        });

        drawingArea.setOnMouseReleased(_ -> {
            if (startPoint[0] != null) {


                try {
                    final Node node = figure.createFromMousePoints(points);
                    if (!ParametersValidator.isFigureInBounds(points, drawingArea.getLayoutBounds(), figure)) {
                        return;
                    }
                    drawingArea.getChildren().add(node);
                    drawingHistory.push(node);
                    redoHistory.clear();
                } catch (IllegalArgumentException e) {
                    MessageController.showAlert("Error", e.getMessage());
                }
                clearPreview();
                startPoint[0] = null;
            }
        });
    }

    private void setupClickHandlers(final MouseDrawable mouseDrawable) {
        points.clear();
        drawingArea.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {

                boolean handleClickCondition = mouseDrawable.getInteractionType() == MouseDrawable.InteractionType.CLICKS &&
                                               !ParametersValidator.isPointOutOfBounds(
                                                       event.getX(),
                                                       event.getY(),
                                                       drawingArea.getLayoutBounds());

                if(handleClickCondition) {
                    handleClick(event.getClickCount(), event.getX(), event.getY(), mouseDrawable);
                }
            }
        });
    }

    private void handleClick(final int clickCount, final double x, final double y, MouseDrawable mouseDrawable) {
        if (clickCount == 1) {
            final Circle dot = new Circle(x, y, 3, Color.RED);
            if(tempDots.size() == mouseDrawable.getPointsCount()) {
                final Circle temp = tempDots.removeFirst();
                points.removeFirst();
                drawingArea.getChildren().remove(temp);
            }
            tempDots.add(dot);
            drawingArea.getChildren().add(dot);
            points.add(new Point2D(x, y));
            updatePreview(mouseDrawable);
        } else if (clickCount == 2) {
            try {
                final Node node = mouseDrawable.createFromMousePoints(points);
                drawingArea.getChildren().add(node);
                drawingHistory.push(node);
                redoHistory.clear();
            } catch (IllegalArgumentException _) {}
            tempDots.forEach(dot -> drawingArea.getChildren().remove(dot));
            tempDots.clear();
            clearPreview();
            points.clear();

        }
    }

    private void updatePreview(final MouseDrawable mouseDrawable) {
        clearPreview();
        final List<Node> previewNodes = new ArrayList<>();

        for (final Point2D point : points) {
            final Circle dot = new Circle(point.getX(), point.getY(), 3, Color.RED);
            previewNodes.add(dot);
        }
        try {
            final Node figureNode = mouseDrawable.createFromMousePoints(points);
            if (figureNode instanceof Shape) {
                ((Shape) figureNode).setStroke(Color.BLUE);
                ((Shape) figureNode).setStrokeWidth(1);
                ((Shape) figureNode).getStrokeDashArray().addAll(5d, 5d);
                ((Shape) figureNode).setFill(Color.TRANSPARENT);
            }
            previewNodes.add(figureNode);

            previewNode = new Group(previewNodes);
            drawingArea.getChildren().add(previewNode);
        } catch (IllegalArgumentException e) { /* Invalid preview state */ }
    }

    private void clearPreview() {
        if (previewNode != null) {
            drawingArea.getChildren().remove(previewNode);
            previewNode = null;
        }
    }

    @Override
    public void undo() {
        if (!drawingHistory.empty()) {
            final Node last = drawingHistory.pop();
            redoHistory.push(last);
            drawingArea.getChildren().remove(last);
        }
    }

    @Override
    public void redo() {
        if (!redoHistory.empty()) {
            final Node node = redoHistory.pop();
            drawingHistory.push(node);
            drawingArea.getChildren().add(node);
        }
    }
}