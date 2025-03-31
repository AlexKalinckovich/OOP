package org.example.oop;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.oop.Figures.*;
import org.example.oop.FiguresView.FigureView;
import org.example.oop.Models.Files.FileManager;
import org.example.oop.Plugins.FigurePlugin;

import java.util.List;
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

        // Создаем View и настраиваем Stage
        final FigureView figureView = new FigureView(figures);
        stage.setTitle("Figure Drawing Application");
        stage.setScene(figureView.getScene());

        final FileManager fileManager = new FileManager();
        final ServiceLoader<FigurePlugin> plugins = ServiceLoader.load(FigurePlugin.class);
        plugins.forEach(plugin -> {
            System.out.println("Loaded plugin: " + plugin.getTypeName());
            figureView.registerFigure(plugin.getTypeName(), plugin.createFigureInstance());
            fileManager.registerPlugin(plugin);
            fileManager.registerSubtypes(plugin.getFigureClass());
        });
        stage.show();
    }

    public static void main(String[] args) { launch(args); }

}