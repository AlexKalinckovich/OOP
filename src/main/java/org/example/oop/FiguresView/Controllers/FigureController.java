package org.example.oop.FiguresView.Controllers;


import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.example.oop.Figures.Figure;
import org.example.oop.FiguresView.DrawingArea;
import org.example.oop.FiguresView.Panels.ControlPanel;
import org.example.oop.FiguresView.Panels.SettingsPanel;
import org.example.oop.FiguresView.Panels.ToolbarPanel;
import org.example.oop.Models.DrawStrategy.DrawStrategy;
import org.example.oop.Models.DrawStrategy.MouseDrawStrategy;
import org.example.oop.Models.Files.FileManager;
import org.example.oop.Models.Settings.FigureSettings;
import org.example.oop.Plugins.FigurePlugin;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FigureController {
    // Мапа фигур для выбора
    private final Map<String, Figure> figureTypes;
    private Figure currentFigure;

    // UI-компоненты (View)
    private final DrawingArea drawingArea;
    private final ControlPanel controlPanel;
    private final ToolbarPanel toolbarPanel;
    private final SettingsPanel settingsPanel;

    // Логика для выбора стратегии отрисовки (Strategy)
    private DrawStrategy currentDrawStrategy;

    public FigureController(Collection<Figure> figures) {
        this.figureTypes = figures.stream()
                .collect(Collectors.toMap(f -> f.getClass().getSimpleName(), f -> f));

        drawingArea = new DrawingArea();
        controlPanel = new ControlPanel(figureTypes.keySet());
        toolbarPanel = new ToolbarPanel();
        settingsPanel = new SettingsPanel();

        initEventHandlers();
        initDrawStrategies();
        selectDefaultFigure();
    }

    public void registerFigure(final String type,final Figure figure) {
        figureTypes.put(type + "Figure", figure);
        controlPanel.addNewFigure(figure);
    }

    /**
     * Инициализируем обработчики событий между компонентами.
     */
    private void initEventHandlers() {
        // Выбор фигуры в панели управления
        controlPanel.setOnFigureSelected(selectedFigureName -> {
            if (figureTypes.containsKey(selectedFigureName)) {
                currentFigure = figureTypes.get(selectedFigureName);
                drawFigure();
            } else {
                throw new IllegalStateException("Неизвестная фигура: " + selectedFigureName);
            }
        });

        // Кнопка "Draw"
        controlPanel.setOnDrawAction(this::drawFigure);

        // Кнопка "Reset"
        controlPanel.setOnResetAction(drawingArea::reset);

        // Обработчики для undo/redo вынесены в ToolbarPanel
        toolbarPanel.setOnUndoAction((_) -> currentDrawStrategy.undo());
        toolbarPanel.setOnRedoAction((_) -> currentDrawStrategy.redo());

        toolbarPanel.setOnPluginAction((_) -> pluginLoad());

        // Обработчики сохранения/загрузки
        toolbarPanel.setOnSaveAction((_) -> FileController.handleSave(drawingArea.getCurrentNodes()));
        toolbarPanel.setOnLoadAction((_) -> FileController.handleLoad(drawingArea.getDrawingPane()));

    }

    private void pluginLoad(){
        Optional<FigurePlugin> pluginOptional = FileController.handleJarPluginLoad();
        if(pluginOptional.isPresent()){
            final FigurePlugin plugin = pluginOptional.get();
            System.out.println("Loaded plugin: " + plugin.getTypeName());
            registerFigure(plugin.getTypeName(), plugin.createFigureInstance());
            FileManager.registerPlugin(plugin);
            FileManager.registerSubtypes(plugin.getFigureClass());
        }
    }

    /**
     * Инициализируем стратегии отрисовки для разных режимов.
     */
    private void initDrawStrategies() {
        currentDrawStrategy = new MouseDrawStrategy(drawingArea.getDrawingPane());
    }

    /**
     * Выбираем фигуру по умолчанию.
     */
    private void selectDefaultFigure() {
        String defaultName = figureTypes.keySet().iterator().next();
        controlPanel.selectFigure(defaultName);
    }


    /**
     * Логика отрисовки фигуры.
     */
    private void drawFigure() {
        if (currentFigure != null) {
            FigureSettings settings = settingsPanel.getSettings();
            currentFigure.setSettings(settings);

            currentDrawStrategy.draw(currentFigure, drawingArea.getDrawingPane().getLayoutBounds());
        }
    }

    /**
     * Собираем основное Scene.
     */
    public Scene getScene() {
        final BorderPane root = new BorderPane();
        root.setTop(toolbarPanel.getToolBar());
        root.setLeft(controlPanel.getPanel());
        root.setCenter(drawingArea.getContainer());
        root.setRight(settingsPanel.getPanel());
        return new Scene(root, 800, 600);
    }
}
