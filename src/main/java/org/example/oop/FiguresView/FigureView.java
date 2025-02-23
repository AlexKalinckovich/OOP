package org.example.oop.FiguresView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.example.oop.Figures.Figure;
import org.example.oop.Models.DrawStrategy.DrawStrategy;
import org.example.oop.Models.Factories.StrategyFactory;
import org.example.oop.Models.Factories.UIFactory;
import org.example.oop.Models.Files.FileManager;
import org.example.oop.Models.PrintingClasses.MessageController;
import org.example.oop.Models.Settings.FigureSettings;
import org.example.oop.Models.ValidationClasses.ParametersValidator;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FigureView {
    private final Map<String, Figure> figureTypes;
    private final MessageController messageController = new MessageController();
    private final ParametersValidator parametersValidator = new ParametersValidator(messageController);
    private final FigureSettings settings = new FigureSettings();
    private final Stack<Node> drawingHistory = new Stack<>();
    private final ToggleGroup drawModeGroup = new ToggleGroup();
    private final RadioButton coordRadio = new RadioButton("By Coordinates");
    private final RadioButton mouseRadio = new RadioButton("By Mouse");


    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private  ComboBox<String> figureSelector;

    private  Button drawButton;
    private  Button resetButton;


    private  BorderPane rootLayout;
    private  GridPane inputArea;

    private ToolBar toolBar;

    private Figure currentFigure;
    private Pane drawingArea;
    private TextField[] parameterFields;
    private Canvas gridCanvas;

    private DrawStrategy currentDrawStrategy;

    public FigureView(final Collection<Figure> figures) {
        this.figureTypes = figures.stream()
                .collect(Collectors.toMap(f -> f.getClass().getSimpleName(), f -> f));
        initializeComponents();
        setDefaultFigure();
        setupRootLayout();
        initDrawStrategies();
        setupDrawModeSwitcher();
    }

    private void setDefaultFigure() {
        final String defaultFigureName = figureTypes.keySet().iterator().next();
        figureSelector.getSelectionModel().select(defaultFigureName);
        figureSelector.setOnAction(_ -> updateCurrentFigure());
        updateCurrentFigure();
    }

    private void updateCurrentFigure() {
        final String selectedFigureName = figureSelector.getValue();
        if(figureTypes.containsKey(selectedFigureName)) {
            currentFigure = figureTypes.get(selectedFigureName);
        }else{
            throw new IllegalStateException();
        }

        // Надо автоматически поменять стратегию, не дожидаясь нажатия на кнопку "Draw"
        if(mouseRadio.isSelected()) {
            drawFigure();
        }

        updateInputFields(currentFigure);
    }

    private void updateInputFields(final Figure figure) {
        inputArea.getChildren().clear();
        inputArea.setVgap(8);
        inputArea.setHgap(8);
        inputArea.setPadding(new Insets(10));

        final int paramCount = figure.getParameterCount();
        parameterFields = new TextField[paramCount];

        for (int i = 0; i < paramCount; i++) {
            final Label label = new Label(figure.getParameterNames()[i]);
            label.setStyle("-fx-font-weight: bold;");

            final TextField field = new TextField();
            field.setStyle("-fx-font-size: 14px;");

            inputArea.addRow(i, label, field);
            parameterFields[i] = field;
        }
    }

    private void initializeComponents() {
        this.rootLayout = new BorderPane();
        this.drawingArea = createDrawingArea();
        this.inputArea = (GridPane) UIFactory.createGridPane();
        this.figureSelector = UIFactory.createComboBox(
                FXCollections.observableArrayList(figureTypes.keySet())
        );
        this.drawButton = UIFactory.createButton("Draw", "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        this.resetButton = UIFactory.createButton("Reset", "-fx-background-color: #f44336; -fx-text-fill: white;");
        this.toolBar = UIFactory.createToolBar();
        setupSettingsPanel();
        setupToolBar();
    }

    private void setupToolBar() {
        final Button saveButton = UIFactory.createIconButton("save.png", "Сохранить");
        saveButton.setOnAction(_ -> handleSave());

        final Button loadButton = UIFactory.createIconButton("load.png", "Загрузить");
        loadButton.setOnAction(_ -> handleLoad());

        final Button undoButton = UIFactory.createIconButton("undo.png", "Отменить");
        undoButton.setOnAction(_ -> handleUndo());

        toolBar.getItems().addAll(saveButton, loadButton, undoButton);
        rootLayout.setTop(toolBar);
    }

    private void handleUndo() {
        if (!drawingHistory.isEmpty()) {
            final Node lastFigure = drawingHistory.pop();
            drawingArea.getChildren().removeIf(node -> node == lastFigure);
        }
    }

    private void setupSettingsPanel() {
        final ColorPicker fillColorPicker = new ColorPicker((Color) settings.getFillColor());
        fillColorPicker.setOnAction(_ -> settings.setFillColor(fillColorPicker.getValue()));

        final ColorPicker strokeColorPicker = new ColorPicker((Color) settings.getStrokeColor());
        strokeColorPicker.setOnAction(_ -> settings.setStrokeColor(strokeColorPicker.getValue()));

        final Spinner<Double> lineWidthSpinner = new Spinner<>(0.1, 10.0, settings.getStrokeWidth(), 0.5);
        lineWidthSpinner.valueProperty().addListener((_, _, newVal) -> settings.setStrokeWidth(newVal));

        final ComboBox<String> lineTypeCombo = new ComboBox<>(FXCollections.observableArrayList("Solid", "Dashed", "Dotted"));
        lineTypeCombo.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            switch (newVal) {
                case "Dashed" -> settings.setDashPattern(List.of(5.0, 5.0));
                case "Dotted" -> settings.setDashPattern(List.of(1.0, 3.0));
                default -> settings.setDashPattern(null);
            }
        });

        final VBox settingsPanel = new VBox(10,
                new Label("Настройки стиля:"),
                new HBox(10, new Label("Заливка:"), fillColorPicker),
                new HBox(10, new Label("Обводка:"), strokeColorPicker),
                new HBox(10, new Label("Толщина:"), lineWidthSpinner),
                new HBox(10, new Label("Тип линии:"), lineTypeCombo)
        );
        settingsPanel.setPadding(new Insets(15));
        settingsPanel.setStyle("-fx-background-color: #f8f8f8;");

        rootLayout.setRight(settingsPanel);
    }

    private void setupRootLayout() {
        final VBox controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #f0f0f0;");

        controlPanel.getChildren().addAll(
                UIFactory.createLabel("Figure Drawer", "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;"),
                figureSelector,
                inputArea,
                createButtonPanel(),
                createModeSwitcher()
        );

        rootLayout.setLeft(controlPanel);
        rootLayout.setCenter(drawingArea);
    }

    private void initDrawStrategies() {
        currentDrawStrategy = StrategyFactory.createStrategy(
                StrategyFactory.StrategyType.COORDINATE,
                parametersValidator,
                messageController,
                parameterFields,
                drawingArea,
                drawingHistory
        );

        coordRadio.setUserData(StrategyFactory.createStrategy(
                StrategyFactory.StrategyType.COORDINATE,
                parametersValidator,
                messageController,
                parameterFields,
                drawingArea,
                drawingHistory
        ));

        mouseRadio.setUserData(StrategyFactory.createStrategy(
                StrategyFactory.StrategyType.MOUSE,
                parametersValidator,
                messageController,
                parameterFields,
                drawingArea,
                drawingHistory
        ));
    }

    private void setupDrawModeSwitcher() {
        coordRadio.setToggleGroup(drawModeGroup);
        mouseRadio.setToggleGroup(drawModeGroup);
        coordRadio.setSelected(true);

        mouseRadio.setOnAction(_ -> drawFigure());

        drawModeGroup.selectedToggleProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                currentDrawStrategy = (DrawStrategy) newVal.getUserData();
                updateInputVisibility();
            }
        });
    }

    private void updateInputVisibility() {
        boolean isCoordinateMode = coordRadio.isSelected();
        inputArea.setVisible(isCoordinateMode);
        inputArea.setManaged(isCoordinateMode);
    }

    private Pane createDrawingArea() {
        final Pane drawingContainer = new Pane();

        gridCanvas = new Canvas();
        gridCanvas.setMouseTransparent(true);
        drawingContainer.getChildren().add(gridCanvas);

        drawingArea = new Pane();
        drawingContainer.getChildren().add(drawingArea);

        drawingContainer.layoutBoundsProperty().addListener((_, _, newVal) -> {
            gridCanvas.setWidth(newVal.getWidth());
            gridCanvas.setHeight(newVal.getHeight());
            drawGrid();
        });

        return drawingContainer;
    }

    private void handleSave() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        final File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            executorService.submit(() -> {
                try {
                    FileManager.saveToFile(drawingHistory, file.toPath());
                } catch (IOException e) {
                    messageController.showAlert("File error","File not found");
                }
            });
        }
    }

    private void handleLoad() {
        final FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            executorService.submit(() -> {
                List<Node> figures;
                try {
                    figures = FileManager.loadFromFile(file.toPath())
                            .stream()
                            .map((Figure::getDrawable))
                            .toList();
                    Platform.runLater(() -> drawingArea.getChildren().setAll(figures));
                } catch (IOException e) {
                    messageController.showAlert("File error","File not found");
                }
            });
        }
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

    private HBox createButtonPanel() {
        final HBox panel = new HBox(10);
        drawButton.setOnAction(_ -> drawFigure());
        resetButton.setOnAction(_ -> {
            drawingArea.getChildren().clear();
            drawingArea.getChildren().add(gridCanvas);
        });

        panel.getChildren().addAll(drawButton, resetButton);
        return panel;
    }

    private HBox createModeSwitcher() {
        final HBox modePanel = new HBox(10, coordRadio, mouseRadio);
        modePanel.setPadding(new Insets(10));
        return modePanel;
    }

    private void drawFigure() {
        currentFigure.setSettings(settings);
        Node temp = null;
        var list = drawingArea.getChildren();
        if(!list.isEmpty()){
            temp = list.getLast();
        }
        currentDrawStrategy.draw(currentFigure, drawingArea.getLayoutBounds());

        if(temp != null && list.getLast() != temp){
            drawingHistory.push(list.getLast());
        }
    }

    public Scene getScene() {
        return new Scene(rootLayout, 800, 600);
    }


}