package org.example.oop.Plugins;

import javafx.scene.Node;
import org.example.oop.Figures.Figure;
import org.example.oop.Models.Files.FigureDTO;

import java.util.function.Function;

public interface FigurePlugin {
    String getTypeName();
    Class<? extends Figure> getFigureClass();
    Function<Node, FigureDTO> getToDTOConverter();
    Function<FigureDTO, Node> getFromDTOConverter();
    Figure createFigureInstance();
}