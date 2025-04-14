package org.example.oop.FiguresView;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class DrawingArea {
    private final StackPane container;
    private final Pane drawingPane;
    private final Canvas gridCanvas;

    public DrawingArea() {
        container = new StackPane();
        drawingPane = new Pane();
        gridCanvas = new Canvas();
        gridCanvas.setMouseTransparent(true);
        container.getChildren().addAll(gridCanvas, drawingPane);

        container.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            gridCanvas.setWidth(newVal.getWidth());
            gridCanvas.setHeight(newVal.getHeight());
            drawGrid();
        });

    }

    private void drawGrid() {
        final GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);
        for (double x = 0; x < gridCanvas.getWidth(); x += 50) {
            gc.strokeLine(x, 0, x, gridCanvas.getHeight());
        }
        for (double y = 0; y < gridCanvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, gridCanvas.getWidth(), y);
        }
    }

    public Pane getDrawingPane() {
        return drawingPane;
    }

    public StackPane getContainer() {
        return container;
    }


    public List<Node> getCurrentNodes() {
        return drawingPane.getChildren();
    }

    public void addNodes(List<Node> nodes) {
        drawingPane.getChildren().addAll(nodes);
    }

    public void reset() {
        drawingPane.getChildren().clear();
        drawingPane.getChildren().add(gridCanvas);
    }


}
