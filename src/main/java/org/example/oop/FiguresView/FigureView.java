package org.example.oop.FiguresView;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.oop.Figures.Figure;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class FigureView {
    private final Map<String, Figure> figureTypes;
    private Figure currentFigure;
    private Pane drawingArea;
    private final GridPane inputArea;
    private TextField[] parameterFields;

    private final ComboBox<String> figureSelector;
    private final Button drawButton;
    private final Button resetButton;
    private final BorderPane rootLayout;

    private Pane drawingContainer;
    private Canvas gridCanvas;

    // Конструктор принимает коллекцию фигур для отображения
    public FigureView(Collection<Figure> figures) {
        this.figureTypes = new HashMap<>();
        figures.forEach(f -> figureTypes.put(f.getClass().getSimpleName(), f));

        // Инициализация компонентов
        this.rootLayout = new BorderPane();
        this.drawingArea = createDrawingArea();
        this.inputArea = new GridPane();
        this.figureSelector = comboBoxInit();
        this.drawButton = buttonsInit();
        this.resetButton = buttonsInit();

        // Настройка корневого лэйаута
        setupRootLayout();
    }

    // Метод для получения готового Scene
    public Scene getScene() {
        return new Scene(rootLayout, 800, 600);
    }

    private void setupRootLayout() {
        // Панель управления с элементами ввода
        final VBox controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #f0f0f0;");

        // Добавляем компоненты
        controlPanel.getChildren().addAll(
                createTitle("Figure Drawer"),
                figureSelector,
                inputArea,
                createButtonPanel()
        );

        rootLayout.setLeft(controlPanel);
        rootLayout.setCenter(drawingArea);
    }

    // Инициализация ComboBox
    private ComboBox<String> comboBoxInit() {
        /*
         * FXCollections.observableArrayList - создает ObservableList,
         * который автоматически обновляет ComboBox при изменениях
         *
         * getSelectionModel().selectFirst() - выбирает первый элемент в списке
         *
         * setOnAction - обработчик изменения выбора
         */
        final ObservableList<String> items = FXCollections.observableArrayList(figureTypes.keySet());
        final ComboBox<String> combo = new ComboBox<>(items);
        combo.getSelectionModel().selectFirst();
        combo.setStyle("-fx-font: 14px 'Arial';");
        combo.setOnAction(_ -> updateInputFields(
                figureTypes.get(combo.getValue())
        ));
        return combo;
    }

    // Инициализация кнопок
    private Button buttonsInit() {
        /*
         * setStyle - устанавливает CSS стили для компонента
         * setPrefWidth - задает предпочтительную ширину
         * setOnAction - обработчик клика
         */
        final Button btn = new Button();
        btn.setPrefWidth(120);
        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        return btn;
    }

    // Обновление полей ввода при выборе фигуры
    private void updateInputFields(final Figure figure) {
        /*
         * getChildren().clear() - очищает все дочерние элементы GridPane
         * addRow() - добавляет строку с компонентами в GridPane
         */
        inputArea.getChildren().clear();
        inputArea.setVgap(8);
        inputArea.setHgap(8);
        inputArea.setPadding(new Insets(10));

        currentFigure = figure;
        final int paramCount = figure.getParameterCount();
        parameterFields = new TextField[paramCount];

        for(int i = 0; i < paramCount; i++) {
            final Label label = new Label(figure.getParameterNames()[i]);
            label.setStyle("-fx-font-weight: bold;");

            final TextField field = new TextField();
            field.setStyle("-fx-font-size: 14px;");

            inputArea.addRow(i, label, field);
            parameterFields[i] = field;
        }
    }

    // Создание области рисования
    private Pane createDrawingArea() {
        /*
         * setStyle - CSS стили для области рисования
         * setBorder - создает границу вокруг панели
         */
        drawingContainer = new Pane();

        // Слой сетки
        gridCanvas = new Canvas();
        gridCanvas.setMouseTransparent(true);
        drawingContainer.getChildren().add(gridCanvas);

        // Слой для фигур
        drawingArea = new Pane();
        drawingContainer.getChildren().add(drawingArea);

        // Обработчики изменения размера
        drawingContainer.widthProperty().addListener(_ -> {
            gridCanvas.setWidth(drawingContainer.getWidth());
            drawGrid();
        });

        drawingContainer.heightProperty().addListener(_ -> {
            gridCanvas.setHeight(drawingContainer.getHeight());
            drawGrid();
        });

        return drawingContainer;
    }

    private void drawGrid() {
        final GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        // Рисование осей
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        // Вертикальные линии
        for (double x = 0; x < gridCanvas.getWidth(); x += 50) {
            gc.strokeLine(x, 0, x, gridCanvas.getHeight());
        }

        // Горизонтальные линии
        for (double y = 0; y < gridCanvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, gridCanvas.getWidth(), y);
        }
    }

    // Создание панели с кнопками
    private HBox createButtonPanel() {
        /*
         * HBox - горизонтальный layout
         * setSpacing - расстояние между элементами
         */
        final HBox panel = new HBox(10);

        drawButton.setText("Draw");
        drawButton.setOnAction(_ -> drawFigure());

        resetButton.setText("Reset");
        resetButton.setStyle("-fx-background-color: #f44336;");
        resetButton.setOnAction(_ -> {
            drawingArea = createDrawingArea();
        });

        panel.getChildren().addAll(drawButton, resetButton);
        return panel;
    }

    // Создание заголовка
    private Label createTitle(String text) {
        /*
         * Label с CSS стилями для заголовка
         */
        final Label label = new Label(text);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        return label;
    }

    private List<String> getFieldsParams(){
        return Arrays.stream(parameterFields)
                .map(TextInputControl::getText)
                .map(String::trim)
                .flatMap(text -> Arrays.stream(text.split("\\s+")))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private boolean isCorrectCoordinates(final double[] params) {

        if(params == null) return false;

        final Bounds bounds = drawingArea.getLayoutBounds();
        final int size = params.length;
        for (int i = 0; i < size; i++) {
            final double val = params[i];
            if (i % 2 == 0 && (val < 0 || val > bounds.getWidth())) {
                showAlert("Ошибка", "X координата выходит за пределы");
                return false;
            }
            if (i % 2 == 1 && (val < 0 || val > bounds.getHeight())) {
                showAlert("Ошибка", "Y координата выходит за пределы");
                return false;
            }
        }
        return true;
    }

    private double[] parseParameters(List<String> params) throws NumberFormatException{
        return params.stream()
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
    // Отрисовка фигуры
    private void drawFigure() {
        boolean isValid = true;
        double[] params = null;
        try {
            params = parseParameters(getFieldsParams());
        }catch (NumberFormatException e){
            isValid = false;
            showAlert("Invalid enters","Values must be numeric");
        }

        if(isValid && isCorrectCoordinates(params)) {
            final Figure figure = figureTypes.get(currentFigure.getClass().getSimpleName());
            try {
                figure.updateParameters(params);
            } catch (IllegalArgumentException _) {
                isValid = false;
                showAlert("Invalid input", "Values must be numeric");
            }
            if(isValid) {
                drawingArea.getChildren().add(figure.getDrawable());
            }
        }
    }

    // Показать диалоговое окно
    private void showAlert(String title, String message) {
        /*
         * Alert - стандартное диалоговое окно
         * AlertType.ERROR - тип окна (иконка ошибки)
         */
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
