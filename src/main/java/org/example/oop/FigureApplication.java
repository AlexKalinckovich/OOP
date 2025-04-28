package org.example.oop;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.oop.Figures.*;
import org.example.oop.FiguresView.Controllers.FigureController;
import org.example.oop.Models.Files.FileManager;
import org.example.oop.Plugins.FigurePlugin;
import org.example.oop.Plugins.PluginConfigRegistry;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class FigureApplication extends Application {

    @Override
    public void start(final Stage stage) {
        final List<Figure> figures = List.of(
                new LineFigure(),
                new TriangleFigure(),
                new RectangleFigure(),
                new CircleFigure(),
                new PolylineFigure(),
                new EllipseFigure(),
                new PolygonFigure()
        );

        final FigureController figureController = new FigureController(figures);
        stage.setTitle("Figure Drawing Application");
        stage.setScene(figureController.getScene());
        PluginConfigRegistry.initializePlugins(figureController);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }

}