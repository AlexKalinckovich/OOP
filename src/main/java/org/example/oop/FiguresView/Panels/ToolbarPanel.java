package org.example.oop.FiguresView.Panels;


import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.example.oop.FiguresView.UIControlsFactory;

import java.util.function.Consumer;

public class ToolbarPanel {
    private final ToolBar toolBar;
    private Consumer<Void> onUndoAction;
    private Consumer<Void> onRedoAction;
    private Consumer<Void> onSaveAction;
    private Consumer<Void> onLoadAction;
    private Consumer<Void> onPluginAction;

    public ToolbarPanel() {
        toolBar = new ToolBar();

        final Button saveButton = UIControlsFactory.createIconButton("save.png", "Сохранить");
        final Button loadButton = UIControlsFactory.createIconButton("load.png", "Загрузить");
        final Button undoButton = UIControlsFactory.createIconButton("undo.png", "Отменить");
        final Button redoButton = UIControlsFactory.createIconButton("redo.png", "Повторить");
        final Button pluginButton = UIControlsFactory.createIconButton("plugin.png","Загрузить плагин");

        saveButton.setOnAction(_ -> { if(onSaveAction != null) onSaveAction.accept(null); });
        loadButton.setOnAction(_ -> { if(onLoadAction != null) onLoadAction.accept(null); });
        undoButton.setOnAction(_ -> { if(onUndoAction != null) onUndoAction.accept(null); });
        redoButton.setOnAction(_ -> { if(onRedoAction != null) onRedoAction.accept(null); });
        pluginButton.setOnAction(_ -> { if(onPluginAction != null) onPluginAction.accept(null); });

        toolBar.getItems().addAll(saveButton, loadButton, undoButton, redoButton,pluginButton);
    }


    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setOnUndoAction(final Consumer<Void> handler) {
        this.onUndoAction = handler;
    }

    public void setOnRedoAction(final Consumer<Void> handler) {
        this.onRedoAction = handler;
    }

    public void setOnSaveAction(final Consumer<Void> handler) {
        this.onSaveAction = handler;
    }

    public void setOnLoadAction(final Consumer<Void> handler) {
        this.onLoadAction = handler;
    }

    public void setOnPluginAction(final Consumer<Void> handler) {this.onPluginAction = handler;}
}
