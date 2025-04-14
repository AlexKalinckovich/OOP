package org.example.oop.FiguresView.Panels;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.oop.Models.Settings.FigureSettings;

import java.util.List;

public class SettingsPanel {
    private final VBox panel;
    private final FigureSettings settings = new FigureSettings();

    public SettingsPanel() {
        panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #f8f8f8;");

        final Label header = new Label("Настройки стиля:");

        final ColorPicker fillColorPicker = new ColorPicker((Color) settings.getFillColor());
        fillColorPicker.setOnAction(_ -> settings.setFillColor(fillColorPicker.getValue()));

        final ColorPicker strokeColorPicker = new ColorPicker((Color) settings.getStrokeColor());
        strokeColorPicker.setOnAction(_ -> settings.setStrokeColor(strokeColorPicker.getValue()));

        final Spinner<Double> lineWidthSpinner = new Spinner<>(0.1, 10.0, settings.getStrokeWidth(), 0.5);
        lineWidthSpinner.valueProperty().addListener((_, _, newVal) -> settings.setStrokeWidth(newVal));

        final ComboBox<String> lineTypeCombo = new ComboBox<>();
        lineTypeCombo.getItems().addAll("Solid", "Dashed", "Dotted");
        lineTypeCombo.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            switch (newVal) {
                case "Dashed":
                    settings.setDashPattern(List.of(5.0, 5.0));
                    break;
                case "Dotted":
                    settings.setDashPattern(List.of(1.0, 3.0));
                    break;
                default:
                    settings.setDashPattern(null);
            }
        });

        panel.getChildren().addAll(
                header,
                new HBox(10, new Label("Заливка:"), fillColorPicker),
                new HBox(10, new Label("Обводка:"), strokeColorPicker),
                new HBox(10, new Label("Толщина:"), lineWidthSpinner),
                new HBox(10, new Label("Тип линии:"), lineTypeCombo)
        );
    }

    public VBox getPanel() {
        return panel;
    }

    public FigureSettings getSettings() {
        return settings;
    }
}
