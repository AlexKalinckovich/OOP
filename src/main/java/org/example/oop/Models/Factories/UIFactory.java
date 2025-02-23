package org.example.oop.Models.Factories;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class UIFactory {
    public static Button createButton(String text, String style) {
        final Button btn = new Button(text);
        btn.setPrefWidth(120);
        btn.setStyle("-fx-font-size: 14px; " + style);
        return btn;
    }

    public static Label createLabel(String text, String style) {
        final Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    public static ComboBox<String> createComboBox(ObservableList<String> items) {
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

    public static Button createIconButton(String iconPath, String tooltip) {
        ImageView icon = new ImageView(new Image(
                                       Objects.requireNonNull(
                                       UIFactory.class.getResourceAsStream("/icons/" + iconPath))));
        icon.setFitWidth(24);
        icon.setFitHeight(24);

        Button btn = new Button();
        btn.setGraphic(icon);
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    public static ToolBar createToolBar() {
        return new ToolBar();
    }

}