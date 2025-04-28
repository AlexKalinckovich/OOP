package org.example.oop.FiguresView.Panels;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.oop.Figures.Figure;
import org.example.oop.FiguresView.UIControlsFactory;

import java.util.Set;
import java.util.function.Consumer;

public class ControlPanel {
    private final VBox panel;
    private final ComboBox<String> figureSelector;
    private Consumer<String> onFigureSelected;
    private Runnable onDrawAction;
    private Runnable onResetAction;


    public ControlPanel(final Set<String> figureNames) {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #f0f0f0;");

        final Label title = new Label("Figure Drawer");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        figureSelector = new ComboBox<>(FXCollections.observableArrayList(figureNames));
        figureSelector.setOnAction(_ -> {
            if (onFigureSelected != null) {
                onFigureSelected.accept(figureSelector.getValue());
            }
        });

        final GridPane inputArea = new GridPane();
        inputArea.setVgap(8);
        inputArea.setHgap(8);
        inputArea.setPadding(new Insets(10));


        // Добавление кнопок Draw и Reset через фабрику контролов.
        final HBox buttonPanel = UIControlsFactory.createButtonPanel(
                () -> { if(onDrawAction != null) onDrawAction.run(); },
                () -> { if(onResetAction != null) onResetAction.run(); }
        );

        panel.getChildren().addAll(title, figureSelector, inputArea, buttonPanel);

    }

    /**
     * Добавляет новую фигуру в список выбора.
     * Для удобства используется имя класса фигуры.
     */
    public void addNewFigure(final Figure figure) {
        // Здесь можно, например, использовать simple name фигуры.
        figureSelector.getItems().add(figure.getClass().getSimpleName());
    }

    public VBox getPanel() {
        return panel;
    }

    /**
     * Выбирает фигуру по имени и вызывает обработчик события.
     */
    public void selectFigure(final String figureName) {
        figureSelector.getSelectionModel().select(figureName);
        if (onFigureSelected != null)
            onFigureSelected.accept(figureName);
    }

    // Методы для назначения обработчиков событий

    public void setOnFigureSelected(final Consumer<String> handler) {
        this.onFigureSelected = handler;
    }

    public void setOnDrawAction(final Runnable handler) {
        this.onDrawAction = handler;
    }

    public void setOnResetAction(final Runnable handler) {
        this.onResetAction = handler;
    }

}
