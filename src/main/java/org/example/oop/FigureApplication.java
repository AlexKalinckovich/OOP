package org.example.oop;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.oop.Figures.*;
import org.example.oop.FiguresView.FigureView;

import java.net.MalformedURLException;
import java.util.List;

public class FigureApplication extends Application {
    @Override
    public void start(final Stage stage) throws MalformedURLException {
        final List<Figure> figures = List.of(
                new LineFigure(),
                new TriangleFigure(),
                new RectangleFigure(),
                new CircleFigure(),
                new PolylineFigure(),
                new EllipseFigure()
        );

        // Создаем View и настраиваем Stage
        final FigureView figureView = new FigureView(figures);
        stage.setTitle("Figure Drawing Application");
        stage.setScene(figureView.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}