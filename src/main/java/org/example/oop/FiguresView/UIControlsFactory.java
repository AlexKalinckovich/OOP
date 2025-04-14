package org.example.oop.FiguresView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class UIControlsFactory {
    public static Button createButton(final String text, final String style) {
        final Button btn = new Button(text);
        btn.setPrefWidth(120);
        btn.setStyle("-fx-font-size: 14px; " + style);
        return btn;
    }

    public static Label createLabel(final String text, final String style) {
        final Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    public static ComboBox<String> createComboBox(final ObservableList<String> items) {
        final ComboBox<String> combo = new ComboBox<>(items);
        combo.setStyle("-fx-font: 14px 'Arial';");
        return combo;
    }

    public static Pane createGridPane() {
        final GridPane pane = new GridPane();
        pane.setVgap(8);
        pane.setHgap(8);
        pane.setPadding(new Insets(10));
        return pane;
    }

    public static Button createIconButton(final String iconPath,
                                          final String tooltip) {
        final ImageView icon = new ImageView(new Image(
                Objects.requireNonNull(
                        UIControlsFactory.class.getResourceAsStream("/icons/" + iconPath))));
        icon.setFitWidth(24);
        icon.setFitHeight(24);

        final Button btn = new Button();
        btn.setGraphic(icon);
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    public static Button createIconButton(final String iconPath,
                                          final String tooltip,
                                          Runnable onClick) {
        final Button btn = createIconButton(iconPath, tooltip);
        if(onClick != null) {
            btn.setOnAction(e -> onClick.run());
        }
        return btn;
    }

    public static ToolBar createToolBar() {
        return new ToolBar();
    }

    public static HBox createButtonPanel(Runnable onDraw, Runnable onReset) {
        final Button drawButton = createButton("Draw", "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        drawButton.setOnAction(_ -> onDraw.run());
        final Button resetButton = createButton("Reset", "-fx-background-color: #f44336; -fx-text-fill: white;");
        resetButton.setOnAction(_ -> onReset.run());
        return new HBox(10, drawButton, resetButton);
    }
}
