package org.example.oop.Models.Files;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.example.oop.Plugins.FigurePlugin;

import java.util.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;

public class JSONConverter {
    private final Map<String, Function<Node, FigureDTO>> nodeToDTOMap = new HashMap<>(
            Map.of(
                    "Circle",this::convertCircleToDTO,
                    "Ellipse",this::convertEllipseToDTO,
                    "Line",this::convertLineToDTO,
                    "Polyline",this::convertPolylineToDTO,
                    "Rectangle",this::convertRectangleToDTO,
                    "Polygon",this::convertTriangleToDTO
            )
    );


    private final Map<String, Function<FigureDTO, Node>> dtoToNodeMap = new HashMap<>(
            Map.of(
                    "Circle",this::convertDTOToCircle,
                    "Ellipse",this::convertDTOtoEllipse,
                    "Line",this::convertDTOToLine,
                    "Polyline",this::convertDTOToPolygon,
                    "Rectangle",this::convertDTOToRectangle,
                    "Polygon",this::convertDTOToPolygon
            )
    );

    public void registerPlugin(final FigurePlugin plugin){
        final String pluginName = plugin.getTypeName();
        nodeToDTOMap.put(pluginName, plugin.getToDTOConverter());
        dtoToNodeMap.put(pluginName, plugin.getFromDTOConverter());
    }

    private final String defaultColor = String.valueOf(Color.BLACK);
    private String getValidColor(final String dtoColor){
        return dtoColor == null ? defaultColor : dtoColor;
    }


    // Методы для преобразования Node в DTO
    private FigureDTO convertCircleToDTO(final Node node) {
        final Circle circle = (Circle) node;
        return new FigureDTO(
                "Circle",
                new double[]{circle.getCenterX(), circle.getCenterY(), circle.getRadius()},
                circle.getFill().toString(),
                String.valueOf(circle.getStroke()),
                circle.getStrokeWidth(),
                circle.getStrokeDashArray()
        );
    }

    private FigureDTO convertEllipseToDTO(final Node node) {
        final Ellipse ellipse = (Ellipse) node;
        return new FigureDTO(
                "Ellipse",
                new double[]{ellipse.getCenterX(),ellipse.getCenterY(),
                             ellipse.getRadiusX(),ellipse.getRadiusY()},
                ellipse.getFill().toString(),
                String.valueOf(ellipse.getStroke()),
                ellipse.getStrokeWidth(),
                ellipse.getStrokeDashArray()
        );
    }

    private FigureDTO convertLineToDTO(final Node node) {
        final Line line = (Line) node;
        return new FigureDTO(
                "Line",
                new double[]{line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()},
                "TRANSPARENT", // Линия не имеет заливки
                String.valueOf(line.getStroke()),
                line.getStrokeWidth(),
                line.getStrokeDashArray()
        );
    }

    private FigureDTO convertPolylineToDTO(final Node node) {
        final Polyline polyline = (Polyline) node;
        final double[] points = polyline.getPoints()
                .stream()
                .flatMapToDouble(DoubleStream::of).toArray();
        return new FigureDTO(
                "Polyline",
                      points,
                      polyline.getFill().toString(),
                      String.valueOf(polyline.getStroke()),
                      polyline.getStrokeWidth(),
                      polyline.getStrokeDashArray()
                );
    }

    private FigureDTO convertRectangleToDTO(final Node node) {
        final Rectangle rect = (Rectangle) node;
        return new FigureDTO(
                "Rectangle",
                new double[]{rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()},
                rect.getFill().toString(),
                String.valueOf(rect.getStroke()),
                rect.getStrokeWidth(),
                rect.getStrokeDashArray()
        );
    }

    private FigureDTO convertTriangleToDTO(Node node) {
        final Polygon triangle = (Polygon) node;

        List<Double> originalPoints = triangle.getPoints();

        List<Double> closedPoints = new ArrayList<>(originalPoints);
        if (originalPoints.size() >= 2) {
            closedPoints.add(originalPoints.get(0)); // x₀
            closedPoints.add(originalPoints.get(1)); // y₀
        }

        double[] pointsArray = closedPoints.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        return new FigureDTO(
                "Polygon",
                pointsArray,
                triangle.getFill().toString(),
                triangle.getStroke().toString(),
                triangle.getStrokeWidth(),
                triangle.getStrokeDashArray()
        );
    }


    // Методы для преобразования DTO в Node
    private Node convertDTOToCircle(FigureDTO dto) {
        double[] params = dto.parameters();
        final String dtoStroke = getValidColor(defaultColor);
        Circle circle = new Circle(params[0], params[1], params[2]);
        circle.setFill(Color.valueOf(dto.fillColor()));
        circle.setStroke(Color.valueOf(dtoStroke));
        circle.setStrokeWidth(dto.strokeWidth());
        circle.getStrokeDashArray().addAll(dto.dashPattern());
        return circle;
    }

    private Node convertDTOToLine(FigureDTO dto) {
        double[] params = dto.parameters();
        final String dtoStroke = getValidColor(defaultColor);
        Line line = new Line(params[0], params[1], params[2], params[3]);
        line.setStroke(Color.valueOf(dtoStroke));
        line.setStrokeWidth(dto.strokeWidth());
        line.getStrokeDashArray().addAll(dto.dashPattern());
        return line;
    }

    private Node convertDTOtoEllipse(final FigureDTO dto){
        final double[] params = dto.parameters();
        final Ellipse ellipse = new Ellipse(
                params[0],params[1],params[2],params[3]
        );
        final String dtoStroke = getValidColor(defaultColor);
        ellipse.setFill(Color.valueOf(dto.fillColor()));
        ellipse.setStroke(Color.valueOf(dtoStroke));
        ellipse.setStrokeWidth(dto.strokeWidth());
        ellipse.getStrokeDashArray().addAll(dto.dashPattern());
        return ellipse;
    }

    private Node convertDTOToPolygon(final FigureDTO dto){
        final Polyline polyline = new Polyline();
        final String dtoStroke = getValidColor(defaultColor);
        polyline.getPoints().addAll(Arrays.stream(dto.parameters()).boxed().toList());
        polyline.setFill(Color.valueOf(dto.fillColor()));
        polyline.setStroke(Color.valueOf(dtoStroke));
        polyline.setStrokeWidth(dto.strokeWidth());
        polyline.getStrokeDashArray().addAll(dto.dashPattern());
        return polyline;
    }

    private Node convertDTOToRectangle(final FigureDTO dto) {
        double[] params = dto.parameters();
        Rectangle rect = new Rectangle(params[0], params[1], params[2], params[3]);
        final String dtoStroke = getValidColor(defaultColor);
        rect.setFill(Color.valueOf(dto.fillColor()));
        rect.setStroke(Color.valueOf(dtoStroke));
        rect.setStrokeWidth(dto.strokeWidth());
        rect.getStrokeDashArray().addAll(dto.dashPattern());
        return rect;
    }

    // Публичные методы для преобразования
    public Optional<FigureDTO> nodeToDTO(Node node) {
        Optional<FigureDTO> result = Optional.empty();
        final String type = node.getClass().getSimpleName();
        final Function<Node, FigureDTO> converter = nodeToDTOMap.get(type);
        if (converter != null) {
            result = Optional.ofNullable(converter.apply(node));
        }
        return result;
    }

    public Optional<Node> dtoToNode(final FigureDTO dto) {
        Optional<Node> result = Optional.empty();
        final Function<FigureDTO, Node> converter = dtoToNodeMap.get(dto.type());
        if (converter != null) {
            result = Optional.ofNullable(converter.apply(dto));
        }
        return result;
    }
}