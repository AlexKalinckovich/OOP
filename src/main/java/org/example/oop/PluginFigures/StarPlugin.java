package org.example.oop.PluginFigures;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.example.oop.Figures.Figure;
import org.example.oop.Models.Files.FigureDTO;
import org.example.oop.Plugins.FigurePlugin;

import java.util.function.Function;
import java.util.stream.DoubleStream;

public class StarPlugin implements FigurePlugin {
    @Override
    public String getTypeName() {
        return "Star";
    }

    @Override
    public Class<? extends Figure> getFigureClass() {
        return StarFigure.class;
    }

    @Override
    public Function<Node, FigureDTO> getToDTOConverter() {
        return node -> {
            Polygon star = (Polygon) node;
            final double[] points = star.getPoints()
                    .stream()
                    .flatMapToDouble(DoubleStream::of).toArray();
            return new FigureDTO(
                    "star",
                    points,
                    String.valueOf(star.getFill()),
                    String.valueOf(star.getStroke()),
                    star.getStrokeWidth(),
                    star.getStrokeDashArray()
            );
        };
    }

    @Override
    public Function<FigureDTO, Node> getFromDTOConverter() {
        return dto -> {
            String fillColor = dto.fillColor();
            String strokeColor = dto.strokeColor();
            fillColor = fillColor == null ? Color.BLACK.toString() : fillColor;
            strokeColor = strokeColor == null ? Color.BLACK.toString() : strokeColor;
            Polygon star = new Polygon(dto.parameters());
            star.setFill(Color.valueOf(fillColor));
            star.setStroke(Color.valueOf(strokeColor));
            star.setStrokeWidth(dto.strokeWidth());
            star.getStrokeDashArray().addAll(dto.dashPattern());
            return star;
        };
    }

    @Override
    public Figure createFigureInstance() {
        return new StarFigure();
    }
}
